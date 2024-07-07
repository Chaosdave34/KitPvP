package io.github.chaosdave34.kitpvp.listener

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import com.mojang.datafixers.util.Pair
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent
import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.items.CustomItemHandler.Companion.getCustomItemId
import io.papermc.paper.event.block.BlockBreakBlockEvent
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import io.papermc.paper.event.entity.EntityMoveEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.block.data.BlockData
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.CrossbowMeta
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

class GameListener : Listener {
    private var blockRemoverTask: BukkitTask? = null

    companion object {
        val blocksToRemove: MutableMap<Location, Pair<Long, BlockData>> = ConcurrentHashMap()
    }

    private fun Cancellable.cancelInGame(entity: Entity) {
        if (entity is Player && ExtendedPlayer.from(entity).inGame()) isCancelled = true
    }

    private fun PlayerEvent.cancelInGame() {
        if (this is Cancellable) this.cancelInGame(this.player)
    }

    private fun EntityEvent.cancelInGame() {
        if (this is Cancellable) this.cancelInGame(this.entity)
    }

    // All
    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) = event.cancelInGame()

    @EventHandler
    fun onFoodLevelChance(event: FoodLevelChangeEvent) = event.cancelInGame()

    @EventHandler
    fun inItemDamage(event: PlayerItemDamageEvent) = event.cancelInGame()


    @EventHandler
    fun onDismountMorph(event: EntityDismountEvent) {
        val player = event.entity
        if (player is Player) {
            val extendedPlayer = ExtendedPlayer.from(player)
            if (extendedPlayer.inGame())
                extendedPlayer.unMorph()
        }
    }

    // Todo: improve for not flying morphs
    @EventHandler
    fun onMorphMove(event: EntityMoveEvent) {
        val morph: Entity = event.entity
        if (morph.hasMetadata("morph")) {
            if (morph.passengers.isEmpty()) morph.remove()
            val player = morph.passengers[0]

            if (player is Player && ExtendedPlayer.from(player).inGame()) {
                morph.velocity = player.eyeLocation.direction.multiply(0.5)
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player is Player) {
            val extendedPlayer = ExtendedPlayer.from(player)
            if (extendedPlayer.inGame() && !event.isCancelled)
                extendedPlayer.enterCombat()
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onDealDamage(event: EntityDealDamageEvent) {
        val player = event.damager
        if (player is Player) {
            val extendedDamager = ExtendedPlayer.from(player)
            if (extendedDamager.inGame() && !event.isCancelled)
                extendedDamager.enterCombat()
        }
    }

    @EventHandler
    fun onRespawnAnchor(event: PlayerInteractEvent) {
        val player = event.player
        if (ExtendedPlayer.from(player).inGame()) {
            val block = event.clickedBlock ?: return
            if (block.type == Material.RESPAWN_ANCHOR) {
                if (event.clickedBlock!!.location.y > 105)
                    event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onRocketLaunch(event: PlayerInteractEvent) {
        val player = event.player
        if (ExtendedPlayer.from(player).inGame()) {
            if (event.material == Material.FIREWORK_ROCKET && event.action == Action.RIGHT_CLICK_BLOCK)
                event.isCancelled = true
        }
    }

    @EventHandler
    fun onLoadCrossbow(event: EntityLoadCrossbowEvent) {
        val player = event.entity
        if (player is Player) {
            val extendedPlayer = ExtendedPlayer.from(player)
            if (extendedPlayer.inGame()) {
                if (event.crossbow.containsEnchantment(Enchantment.INFINITY)) event.setConsumeItem(false)

                val crossbow = event.crossbow
                if (getCustomItemId(crossbow) == CustomItemHandler.ROCKET_LAUNCHER.id || extendedPlayer.gameState == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
                    Bukkit.getScheduler().runTaskLater(KitPvp.INSTANCE, Runnable {
                        crossbow.editMeta(CrossbowMeta::class.java) { crossbowMeta ->
                            val projectiles: MutableList<ItemStack> = mutableListOf()
                            for (projectile in crossbowMeta.chargedProjectiles) {
                                if (projectile.type == Material.FIREWORK_ROCKET) {
                                    projectile.editMeta(FireworkMeta::class.java) { fireworkMeta: FireworkMeta ->
                                        val random = Random()
                                        val randomColor = Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256))
                                        val randomType = FireworkEffect.Type.entries[random.nextInt(FireworkEffect.Type.entries.size)]

                                        fireworkMeta.addEffect(FireworkEffect.builder().withColor(randomColor).with(randomType).build())
                                    }
                                    projectiles.add(projectile)
                                }
                            }
                            if (projectiles.isNotEmpty()) crossbowMeta.setChargedProjectiles(projectiles)
                        }
                    }, 1)
                }
            }
        }
    }

    @EventHandler
    fun onOpenAnvil(event: InventoryOpenEvent) {
        val player = event.player
        if (player is Player && ExtendedPlayer.from(player).inGame()) {
            if (event.inventory is AnvilInventory)
                event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        event.droppedExp = 0
        event.drops.clear()
    }

    // KitPvP
    private class BlockRemover : BukkitRunnable() {
        override fun run() {
            if (blocksToRemove.isEmpty())
                this.cancel()

            val currentTime = System.currentTimeMillis()
            val iterator: MutableIterator<Map.Entry<Location, Pair<Long, BlockData>>> = blocksToRemove.entries.iterator()

            while (iterator.hasNext()) {
                val entry = iterator.next()
                if (currentTime - entry.value.first >= TIMER * 1000) {
                    val block = entry.key.block
                    block.blockData = entry.value.second
                    iterator.remove()
                }
            }
        }

        companion object {
            private const val TIMER = 45
        }
    }

    private fun startBlockRemover() {
        val taskID = blockRemoverTask?.taskId ?: return
        if (!Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            blockRemoverTask = BlockRemover().runTaskTimer(KitPvp.INSTANCE, 0, 20)
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        if (ExtendedPlayer.from(player).gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {
            val block = event.block

            if (event.block.type == Material.FIRE) return

            if (event.block.location.y > 105) {
                player.sendMessage(Component.text("You can't place blocks up here!", NamedTextColor.RED))
                event.isCancelled = true
                return
            }

            block.setMetadata("placed_by_player", FixedMetadataValue(KitPvp.INSTANCE, true))

            if (event.blockReplacedState.hasMetadata("placed_by_player")) blocksToRemove[block.location] =
                Pair(System.currentTimeMillis(), Material.AIR.createBlockData())
            else blocksToRemove[block.location] = Pair(System.currentTimeMillis(), event.blockReplacedState.blockData)

            startBlockRemover()
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (ExtendedPlayer.from(event.player).gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {
            if (!event.block.hasMetadata("placed_by_player")) {
                if (event.block.type == Material.FIRE) return

                event.isCancelled = true
            } else {
                blocksToRemove.remove(event.block.location)
            }

            if (event.block.type == Material.COBWEB) event.isDropItems = false
        }
    }

    @EventHandler
    fun onGravityBlockChangeState(event: EntityChangeBlockEvent) {
        val fallingBlock = event.entity
        if (event.entity is FallingBlock) {
            val block = event.block

            if (fallingBlock.hasMetadata("placed_by_player")) {
                block.setMetadata("placed_by_player", FixedMetadataValue(KitPvp.INSTANCE, true))
                blocksToRemove[block.location] = Pair(System.currentTimeMillis(), event.block.blockData)
            } else if (block.hasMetadata("placed_by_player")) {
                fallingBlock.setMetadata("placed_by_player", FixedMetadataValue(KitPvp.INSTANCE, true))
                blocksToRemove.remove(block.location)
            }
        }
    }

    @EventHandler
    fun onBlockBreakCobWeb(event: BlockBreakBlockEvent) {
        if (event.source.type == Material.WATER) {
            if (event.block.type == Material.COBWEB) event.drops.clear()
        }
    }

    @EventHandler
    fun onBlockBreakGrass(event: BlockFromToEvent) {
        if (event.block.type == Material.WATER) {
            val target = event.toBlock
            if (target.type != Material.AIR) {
                if (!target.hasMetadata("placed_by_player")) {
                    if (target.type == Material.FIRE) return

                    event.isCancelled = true
                } else {
                    blocksToRemove.remove(target.location)
                }
            }
        }
    }

    @EventHandler
    fun onBucketEmpty(event: PlayerBucketEmptyEvent) {
        if (ExtendedPlayer.from(event.player).gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {
            val block = event.block

            if (event.block.location.y > 105) {
                event.isCancelled = true
                return
            }

            block.setMetadata("placed_by_player", FixedMetadataValue(KitPvp.INSTANCE, true))

            if (event.block.hasMetadata("placed_by_player")) blocksToRemove[block.location] =
                Pair(System.currentTimeMillis(), Material.AIR.createBlockData())
            else blocksToRemove[block.location] = Pair(System.currentTimeMillis(), event.block.blockData)

            startBlockRemover()
        }
    }

    @EventHandler
    fun onBucketFill(event: PlayerBucketFillEvent) {
        if (ExtendedPlayer.from(event.player).gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {
            if (!event.block.hasMetadata("placed_by_player")) {
                event.isCancelled = true
            } else {
                blocksToRemove.remove(event.block.location)
            }
        }
    }

    @EventHandler
    fun onModifyArmor(event: InventoryClickEvent) {
        val player = event.whoClicked
        if (player is Player && ExtendedPlayer.from(player).gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {
            if (event.clickedInventory is PlayerInventory && event.slot in 36..39)
                event.isCancelled = true
        }
    }

    // Elytra
    @EventHandler
    fun onBlockPlaceElytra(event: BlockPlaceEvent) {
        if (ExtendedPlayer.from(event.player).gameState == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreakElytra(event: BlockBreakEvent) {
        if (ExtendedPlayer.from(event.player).gameState == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBucketEmptyElytra(event: PlayerBucketEmptyEvent) {
        if (ExtendedPlayer.from(event.player).gameState == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBucketFillElytra(event: PlayerBucketFillEvent) {
        if (ExtendedPlayer.from(event.player).gameState == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            event.isCancelled = true
        }
    }

    // Temporarily switched to vanilla damage type https://github.com/PaperMC/Paper/issues/11036
    @EventHandler
    fun onPlayerMoveElytra(event: PlayerMoveEvent) {
        val player = event.player
        if (ExtendedPlayer.from(player).gameState == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            val blockBelow = event.to.clone().subtract(0.0, 1.0, 0.0).block.type
            val filterForBlockBelow = listOf(Material.GRASS_BLOCK, Material.SAND, Material.SPRUCE_LEAVES, Material.OAK_LEAVES)

            val maxDamage = Float.MAX_VALUE.toDouble()
            if (event.to.block.type == Material.WATER)
                player.damage(maxDamage, DamageSource.builder(DamageType.DROWN).build())
            else if (filterForBlockBelow.contains(blockBelow))
                player.damage(maxDamage, DamageSource.builder(DamageType.FALL).build()) // DamageTypes.LAND
            else if (event.to.y > 199) player.damage(maxDamage, DamageSource.builder(DamageType.OUTSIDE_BORDER).build()) // DamageTypes.ESCAPE

            player.isGlowing = blockBelow == Material.GREEN_WOOL || event.to.clone().subtract(0.0, 2.0, 0.0).block.type == Material.GREEN_WOOL
        }
    }

    @EventHandler
    fun onRocketLaunch(event: PlayerElytraBoostEvent) {
        if (ExtendedPlayer.from(event.player).gameState == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            event.setShouldConsume(false)
        }
    }
}
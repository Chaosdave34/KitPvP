package io.github.chaosdave34.kitpvp.listener

import com.google.common.io.ByteStreams
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.*
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class SpawnListener : Listener {
    private lateinit var turbine1: Location
    private lateinit var turbine2: Location
    private lateinit var turbine3: Location
    private lateinit var turbine4: Location

    private fun Cancellable.cancelInSpawn(entity: Entity) {
        if (entity is Player && ExtendedPlayer.from(entity).inSpawn()) isCancelled = true
    }

    private fun PlayerEvent.cancelInSpawn() {
        if (this is Cancellable) this.cancelInSpawn(this.player)
    }

    private fun EntityEvent.cancelInSpawn() {
        if (this is Cancellable) this.cancelInSpawn(this.entity)
    }

    // ALL Spawns
    @EventHandler
    fun onRespawnAnchorExplode(event: BlockExplodeEvent) {
        val block = event.explodedBlockState ?: return
        if (block.type == Material.RESPAWN_ANCHOR) {
            if (event.block.location.y > 105) event.isCancelled = true
        }
    }

    @EventHandler
    fun onRespawnAnchor(event: PlayerInteractEvent) {
        val player = event.player
        if (ExtendedPlayer.from(player).inSpawn()) {
            val block = event.clickedBlock ?: return
            if (block.type == Material.RESPAWN_ANCHOR)
                event.isCancelled = true
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player is Player) {
            if (ExtendedPlayer.from(player).inSpawn()) {
                // Kill Command
                if (event.cause == EntityDamageEvent.DamageCause.KILL) return
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onDealDamage(event: EntityDealDamageEvent) = event.cancelInSpawn(event.damager)

    @EventHandler
    fun onEnterEndPortal(event: PlayerPortalEvent) {
        val player = event.player
        event.isCancelled = true

        if (ExtendedPlayer.from(player).inSpawn()) {
            if (event.cause == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
                val message = ByteStreams.newDataOutput()
                message.writeUTF("Connect")
                message.writeUTF("amusementpark")
                player.sendPluginMessage(KitPvp.INSTANCE, "BungeeCord", message.toByteArray())

                player.teleport(Location(Bukkit.getWorld("world"), -12.0, 120.0, 16.0))
                player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4))
                player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 20 * 3, 4))
            }
        }
    }

    @EventHandler
    fun onProjectileLaunch(event: ProjectileLaunchEvent) = event.cancelInSpawn()

    @EventHandler
    fun onBowShot(event: EntityShootBowEvent) = event.cancelInSpawn()

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) = event.cancelInSpawn(event.player)

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) = event.cancelInSpawn(event.player)

    @EventHandler
    fun onBucketEmpty(event: PlayerBucketEmptyEvent) = event.cancelInSpawn()

    @EventHandler
    fun onBucketFill(event: PlayerBucketFillEvent) = event.cancelInSpawn()

    @EventHandler
    fun onConsumeItem(event: PlayerItemConsumeEvent) = event.cancelInSpawn()

    @EventHandler
    fun onFoodLevelChance(event: FoodLevelChangeEvent) = event.cancelInSpawn()

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) = event.cancelInSpawn()

    @EventHandler
    fun onLoadCrossbow(event: EntityLoadCrossbowEvent) = event.cancelInSpawn()

    @EventHandler
    fun onOpenInventory(event: InventoryOpenEvent) = event.cancelInSpawn(event.player)

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (ExtendedPlayer.from(player).inSpawn()) {
            if (event.material == Material.TRIDENT) event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val extendedPlayer = ExtendedPlayer.from(player)
        // Normal
        if (extendedPlayer.gameState == ExtendedPlayer.GameState.KITS_SPAWN) {
            // Game enter
            if (event.to.clone().subtract(0.0, 1.0, 0.0).block.type != Material.AIR && event.to.y <= 105) {
                extendedPlayer.gameState = ExtendedPlayer.GameState.KITS_IN_GAME
                player.fallDistance = 0f
            }

            // Turbine
            val location = player.location
            val distance = 5
            if (location.distance(turbine1) < distance || location.distance(turbine2) < distance || location.distance(turbine3) < distance || location.distance(
                    turbine4
                ) < distance
            ) {
                val launchVector = player.location.toVector().normalize()
                launchVector.multiply(12)
                launchVector.setY(0.75)
                player.velocity = launchVector
            }
        } else if (extendedPlayer.gameState == ExtendedPlayer.GameState.ELYTRA_SPAWN) {
            if (event.to.y < 197) {
                extendedPlayer.gameState = ExtendedPlayer.GameState.ELYTRA_IN_GAME
            }
        }
    }

    // Game Spawn
    @EventHandler
    fun onWorldLoad(event: WorldLoadEvent) {
        if (event.world.name != "world") return

        val world = event.world
        turbine1 = Location(world, 2.0, 120.0, 22.0)
        turbine2 = Location(world, -18.0, 120.0, 2.0)
        turbine3 = Location(world, 2.0, 120.0, -18.0)
        turbine4 = Location(world, 22.0, 120.0, 2.0)

        object : BukkitRunnable() {
            override fun run() {
                world.spawnParticle(Particle.POOF, turbine1, 10, 2.0, 1.0, 2.0, 0.0)
                world.spawnParticle(Particle.POOF, turbine2, 10, 2.0, 1.0, 2.0, 0.0)
                world.spawnParticle(Particle.POOF, turbine3, 10, 2.0, 1.0, 2.0, 0.0)
                world.spawnParticle(Particle.POOF, turbine4, 10, 2.0, 1.0, 2.0, 0.0)
            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, 5)
    }

    @EventHandler
    fun onModifyArmor(event: InventoryClickEvent) {
        val p = event.whoClicked as Player
        if (ExtendedPlayer.from(p).gameState == ExtendedPlayer.GameState.KITS_SPAWN) {
            if (event.clickedInventory is PlayerInventory && event.slot in 36..39)
                event.isCancelled = true
        }
    }
}
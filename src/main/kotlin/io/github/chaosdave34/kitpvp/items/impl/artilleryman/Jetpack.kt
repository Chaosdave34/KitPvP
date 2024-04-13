package io.github.chaosdave34.kitpvp.items.impl.artilleryman

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*

class Jetpack : CustomItem(Material.LEATHER_CHESTPLATE, "jetpack") {
    companion object {
        private val refillTasks: MutableMap<UUID, Int> = mutableMapOf()
    }
    private val fuelUsedTime: MutableMap<UUID, Long> = mutableMapOf()

    override fun getName(): Component = createSimpleItemName("Jetpack")

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4)
        itemStack.setLeatherArmorColor(Color.NAVY)
    }

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent) {
        val player = event.player
        val extendedPlayer = ExtendedPlayer.from(player)

        if (extendedPlayer.inGame()) {
            val jetpack = player.inventory.chestplate ?: return

            if (jetpack.isThisCustomItem()) {
                if (event.isSneaking) {
                    val taskId = refillTasks.remove(player.uniqueId)
                    if (taskId != null) Bukkit.getScheduler().cancelTask(taskId)

                    val jetpackMeta = jetpack.itemMeta as Damageable

                    if (jetpackMeta.damage < 80 && player.velocity.length() < 0.1) {
                        player.velocity = player.velocity.setY(0.5)
                    }
                } else {
                    if (!refillTasks.containsKey(player.uniqueId)) {
                        val task: BukkitTask = RefillTask(player).runTaskTimer(KitPvp.INSTANCE, 20, 20)
                        refillTasks[player.uniqueId] = task.taskId
                    }
                }
            }
        }
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val extendedPlayer = ExtendedPlayer.from(player)

        if (extendedPlayer.inGame()) {
            val jetpack = player.inventory.chestplate ?: return

            if (jetpack.isThisCustomItem()) {
                if (player.isSneaking) {
                    val jetpackMeta = jetpack.itemMeta as Damageable

                    val verticalMovement = event.to.clone().subtract(event.from).toVector().setY(player.velocity.y)

                    if (jetpackMeta.damage < 80) {
                        if (player.velocity.y <= 0.3) {
                            if (player.velocity.y < 0) player.velocity = player.velocity.add(Vector(0.0, 0.1, 0.0))
                            else player.velocity = player.velocity.add(Vector(0.0, 0.3, 0.0))

                            if (fuelUsedTime[player.uniqueId]?.let { System.currentTimeMillis() - it > 25 } != false) {
                                jetpackMeta.damage += 1
                                jetpack.setItemMeta(jetpackMeta)
                                fuelUsedTime[player.uniqueId] = System.currentTimeMillis()
                            }
                        }

                        if (verticalMovement.length() > 0.01) {
                            player.velocity = player.eyeLocation.direction.multiply(0.5).setY(player.velocity.y)
                        }

                        player.sendActionBar(Component.text("Jetpack Fuel: " + (80 - jetpackMeta.damage) + "/80"))
                    }
                }
            }
        }
    }


    @EventHandler
    fun onSpawn(event: PlayerSpawnEvent) {
        val player = event.player
        val taskId = refillTasks.remove(player.uniqueId)
        if (taskId != null) Bukkit.getScheduler().cancelTask(taskId)
    }

    class RefillTask(private val player: Player) : BukkitRunnable() {
        override fun run() {
            val jetpack = player.inventory.chestplate ?: return
            val jetpackMeta = jetpack.itemMeta as Damageable

            if (jetpackMeta.damage != 0) {
                jetpackMeta.damage -= 1
                jetpack.setItemMeta(jetpackMeta)
                player.sendActionBar(Component.text("Jetpack Fuel: " + (80 - jetpackMeta.damage) + "/80"))
            } else {
                refillTasks.remove(player.uniqueId)
                this.cancel()
            }
        }
    }
}
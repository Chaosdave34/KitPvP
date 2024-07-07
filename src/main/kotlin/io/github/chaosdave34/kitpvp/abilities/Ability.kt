package io.github.chaosdave34.kitpvp.abilities

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.utils.Describable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


abstract class Ability(val id: String, val name: String, val cooldown: Int, val manaCost: Int, val icon: Material) : Listener, Describable {
    private var playerCooldown: MutableMap<UUID, Long> = mutableMapOf()

    abstract fun getDescription(): List<Component>

    abstract fun onAbility(player: Player): Boolean

    fun getItem(): ItemStack {
        val itemStack = ItemStack.of(icon)

        val lore: MutableList<Component> = mutableListOf(Component.text("Ability:", NamedTextColor.GREEN).append(Component.text(name, NamedTextColor.GOLD)))
        lore.addAll(getDescription())

        itemStack.editMeta {
            it.displayName(Component.text(name, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            it.persistentDataContainer.set(NamespacedKey(KitPvp.INSTANCE, "ability"), PersistentDataType.STRING, id)
            it.lore(lore)
        }

        return itemStack
    }

    fun handleAbility(player: Player) {
        if (player.gameMode == GameMode.SPECTATOR) return

        if (playerCooldown.containsKey(player.uniqueId)) {
            player.sendMessage(Component.text("This ability is on cooldown for " + playerCooldown[player.uniqueId] + "s.", NamedTextColor.RED))
        } else {
            val success = onAbility(player)

            if (success) {
//                if (KitPvp.INSTANCE.customEventHandler.activeEvent === CustomEventHandler.HALVED_COOLDOWN_EVENT) playerCooldown[player.uniqueId] =
//                    max((cooldown / 2).toDouble(), 1.0).toLong()
//                else playerCooldown[player.uniqueId] = cooldown.toLong()

                playerCooldown[player.uniqueId] = cooldown.toLong()

                object : BukkitRunnable() {
                    override fun run() {
                        var currentCooldown = playerCooldown[player.uniqueId] ?: return
                        currentCooldown--
                        playerCooldown[player.uniqueId] = currentCooldown
                        if (currentCooldown == 0L) {
                            playerCooldown.remove(player.uniqueId)

                            val availableMessage: Component = Component.text("Ability ", NamedTextColor.GREEN)
                                .append(Component.text(name, NamedTextColor.GOLD))
                                .append(Component.text(" is now available.", NamedTextColor.GREEN))
                            player.sendMessage(availableMessage)
                            this.cancel()
                        }
                    }
                }.runTaskTimer(KitPvp.INSTANCE, 20, 20)
            }
        }
    }

    protected fun Entity.checkTargetIfPlayer(): Boolean = (this is Player && ExtendedPlayer.from(this).inGame()) || this !is Player
}
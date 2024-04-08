package io.github.chaosdave34.kitpvp.abilities

import io.github.chaosdave34.ghutils.persistentdatatypes.StringArrayPersistentDataType
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.customevents.CustomEventHandler
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.max


abstract class Ability(val id: String, val name: String, val type: Type, val cooldown: Int) : Listener {
    private var playerCooldown: MutableMap<UUID, Long> = mutableMapOf()

    abstract fun getDescription(): List<Component>

    abstract fun onAbility(player: Player): Boolean

    fun apply(itemStack: ItemStack) {
        val itemMeta = itemStack.itemMeta
        val container = itemMeta.persistentDataContainer

        val key = NamespacedKey(KitPvp.INSTANCE, "abilities")

        val currentAbilities = container.get(key, StringArrayPersistentDataType())
        container.set(key, StringArrayPersistentDataType(), currentAbilities?.plus(id) ?: arrayOf(id))

        itemStack.itemMeta = itemMeta
    }

    fun handleAbility(player: Player) {
        if (player.gameMode == GameMode.SPECTATOR) return

        if (playerCooldown.containsKey(player.uniqueId)) {
            player.sendMessage(Component.text("This ability is on cooldown for " + playerCooldown[player.uniqueId] + "s.", NamedTextColor.RED))
        } else {
            val success = onAbility(player)

            if (success) {
                if (KitPvp.INSTANCE.customEventHandler.activeEvent === CustomEventHandler.HALVED_COOLDOWN_EVENT) playerCooldown[player.uniqueId] =
                    max((cooldown / 2).toDouble(), 1.0).toLong()
                else playerCooldown[player.uniqueId] = cooldown.toLong()

                object : BukkitRunnable() {
                    override fun run() {
                        var currentCooldown = playerCooldown[player.uniqueId]!!
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

    protected fun createSimpleDescription(vararg lines: String): List<Component> {
        val componentList: MutableList<Component> = ArrayList()
        for (line in lines) componentList.add(Component.text(line, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
        return componentList
    }

    protected fun checkTargetIfPlayer(target : Entity) : Boolean = (target is Player && ExtendedPlayer.from(target).inGame()) || target !is Player

    enum class Type(val displayName: String) {
        RIGHT_CLICK("RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
        SNEAK_RIGHT_CLICK("SNEAK RIGHT CLICK"),
        SNEAK_LEFT_CLICK("SNEAK LEFT CLICK"),
        SNEAK("SNEAK")
    }
}
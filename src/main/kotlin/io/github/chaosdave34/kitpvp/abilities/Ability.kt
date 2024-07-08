package io.github.chaosdave34.kitpvp.abilities

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.utils.Describable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*


abstract class Ability(val id: String, val name: String, val cooldown: Int, val manaCost: Int, val icon: Material) : Listener, Describable {
    private var playerCooldown: MutableList<UUID> = mutableListOf()

    abstract fun getDescription(): List<Component>

    abstract fun onAbility(player: Player): Boolean

    fun getItem(): ItemStack {
        val itemStack = ItemStack.of(icon)

        val name = Component.text("Ability: ", NamedTextColor.GREEN)
            .append(Component.text(name, NamedTextColor.GOLD))
            .decoration(TextDecoration.ITALIC, false)

        val lore = getDescription() as MutableList
        lore.add(
            Component.text("Cooldown: ", NamedTextColor.DARK_GRAY)
                .append(Component.text("${cooldown}s", NamedTextColor.GREEN))
                .decoration(TextDecoration.ITALIC, false)
        )
        lore.add(
            Component.text("Mana Cost: ", NamedTextColor.DARK_GRAY)
                .append(Component.text("$manaCost ʬ", NamedTextColor.DARK_AQUA))
                .decoration(TextDecoration.ITALIC, false)
        )

        itemStack.editMeta {
            it.displayName(name)
            it.lore(lore)
            it.persistentDataContainer.set(NamespacedKey(KitPvp.INSTANCE, "ability"), PersistentDataType.STRING, id)
        }

        return itemStack
    }

    fun handleAbility(player: Player) {
        if (player.gameMode == GameMode.SPECTATOR) return

        if (playerCooldown.contains(player.uniqueId)) {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 0.5f)
        } else {
            val success = onAbility(player)

            if (success) {
                player.setCooldown(icon, cooldown * 20)
                playerCooldown.add(player.uniqueId)

                Bukkit.getScheduler().runTaskLater(KitPvp.INSTANCE, Runnable { playerCooldown.remove(player.uniqueId) }, cooldown * 20L)
            }
        }
    }

    protected fun Entity.checkTargetIfPlayer(): Boolean = (this is Player && ExtendedPlayer.from(this).inGame()) || this !is Player
}
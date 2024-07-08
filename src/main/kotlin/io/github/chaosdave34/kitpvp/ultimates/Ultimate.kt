package io.github.chaosdave34.kitpvp.ultimates

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.utils.Describable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

abstract class Ultimate(val id: String, val name: String, val damageStackCost: Double, val icon: Material) : Listener, Describable {
    abstract fun getDescription(): List<Component>

    abstract fun onAbility(player: Player): Boolean

    fun getItem(): ItemStack {
        val itemStack = ItemStack.of(icon)

        val name = Component.text("Ultimate: ", NamedTextColor.RED)
            .append(Component.text(name, NamedTextColor.GOLD))
            .decoration(TextDecoration.ITALIC, false)

        val lore = getDescription() as MutableList
        lore.add(
            Component.text("Damage Stack Cost: ", NamedTextColor.DARK_GRAY)
                .append(Component.text("$damageStackCost ☄", NamedTextColor.DARK_RED))
                .decoration(TextDecoration.ITALIC, false)
        )

        itemStack.editMeta {
            it.displayName(name)
            it.lore(lore)
            it.persistentDataContainer.set(NamespacedKey(KitPvp.INSTANCE, "ultimate"), PersistentDataType.STRING, id)
        }

        return itemStack
    }

    fun handleUltimate(player: Player) {
        if (player.gameMode == GameMode.SPECTATOR) return
        val extendedPlayer = ExtendedPlayer.from(player)

        val currentStack = extendedPlayer.attributes.damageStack
        if (currentStack >= damageStackCost) {
            val success = onAbility(player)

            if (success) {
                extendedPlayer.attributes.damageStack = currentStack - damageStackCost
            }
        } else {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1f, 0.5f)
        }
    }

    protected fun Entity.checkTargetIfPlayer(): Boolean = (this is Player && ExtendedPlayer.from(this).inGame()) || this !is Player
}
package io.github.chaosdave34.kitpvp.pasives

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.utils.Describable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

abstract class Passive(val id: String,val name: String,val icon: Material): Describable {
    abstract fun getDescription(): List<Component>

    fun getItem(): ItemStack {
        val itemStack = ItemStack.of(icon)

        val name = Component.text("Passive: ", NamedTextColor.LIGHT_PURPLE)
            .append(Component.text(name, NamedTextColor.GOLD))
            .decoration(TextDecoration.ITALIC, false)

        itemStack.editMeta {
            it.displayName(name)
            it.lore(getDescription())
            it.persistentDataContainer.set(NamespacedKey(KitPvp.INSTANCE, "ability"), PersistentDataType.STRING, id)
        }

        return itemStack
    }
}
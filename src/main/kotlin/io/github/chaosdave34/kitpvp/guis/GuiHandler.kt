package io.github.chaosdave34.kitpvp.guis

import io.github.chaosdave34.kitpvp.KitPvp
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta

// Todo further improve gui framework (but it works and suffices rn)
class GuiHandler : Listener {
    val guis: MutableList<Gui> = mutableListOf()

    companion object {
        fun ItemMeta.hideAttributes() {
            this.addAttributeModifier(
                Attribute.GENERIC_LUCK,
                AttributeModifier(NamespacedKey(KitPvp.INSTANCE, "remove_attributes"), 20.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY)
            )
            this.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        for (gui in guis) {
            for ((_, page) in gui.pages) {
                if (page.first.inventory == event.inventory) {
                    page.first.handleEvent(event)
                    event.isCancelled = true
                }
            }
        }
    }
}
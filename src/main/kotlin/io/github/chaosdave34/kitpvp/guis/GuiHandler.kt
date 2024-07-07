package io.github.chaosdave34.kitpvp.guis

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

// Todo further improve gui framework (but it works and suffices rn)
class GuiHandler : Listener {
    val guis: MutableList<Gui> = mutableListOf()


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
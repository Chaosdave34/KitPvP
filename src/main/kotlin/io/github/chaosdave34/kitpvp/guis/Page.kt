package io.github.chaosdave34.kitpvp.guis

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

class Page(val rows: Int, title: Component) {
    val inventory = Bukkit.createInventory(null, rows * 9, title)

    val eventHandlers: MutableMap<Int, MutableMap<ClickType, MutableList<Consumer<InventoryClickEvent>>>> = mutableMapOf()

    fun createEntry(slot: Int, itemStack: ItemStack) {
        if (slot >= inventory.size) return
        inventory.setItem(slot, itemStack)
    }

    fun createEntry(slot: Int, material: Material, name: Component, function: Consumer<ItemStack>) {
        val itemStack = ItemStack.of(material)
        itemStack.editMeta { it.displayName(name) }

        function.accept(itemStack)
        createEntry(slot, itemStack)
    }

    fun createButton(slot: Int, itemStack: ItemStack, vararg clickTypes: ClickType, eventHandler: Consumer<InventoryClickEvent>) {
        createEntry(slot, itemStack)

        for (clickType in clickTypes) {
            val map = eventHandlers[slot] ?: mutableMapOf()

            val list = map[clickType] ?: mutableListOf()
            list.add(eventHandler)
            map[clickType] = list

            eventHandlers[slot] = map
        }
    }

    fun createButton(slot: Int, itemStack: ItemStack, clickTypeGroups: ClickTypeGroups = ClickTypeGroups.ALL, eventHandler: Consumer<InventoryClickEvent>) {
        createButton(slot, itemStack, *clickTypeGroups.clickTypes, eventHandler = eventHandler)
    }

    fun createButton(slot: Int, material: Material, name: Component, vararg clickTypes: ClickType, eventHandler: Consumer<InventoryClickEvent>) {
        val itemStack = ItemStack.of(material)

        itemStack.editMeta { it.displayName(name) }
        createButton(slot, itemStack, *clickTypes, eventHandler = eventHandler)
    }

    fun createButton(
        slot: Int,
        material: Material,
        name: Component,
        clickTypeGroups: ClickTypeGroups = ClickTypeGroups.ALL,
        eventHandler: Consumer<InventoryClickEvent>
    ) {
        createButton(slot, material, name, *clickTypeGroups.clickTypes, eventHandler = eventHandler)
    }

    fun handleEvent(event: InventoryClickEvent) {
        eventHandlers[event.slot]?.get(event.click)?.forEach { it.accept(event) }
    }

    fun fillEmpty(material: Material = Material.GRAY_STAINED_GLASS_PANE) {
        val content = inventory.contents

        for (slot in content.indices) {
            if (content[slot] == null) {
                val itemStack = ItemStack.of(material)
                itemStack.editMeta { it.isHideTooltip = true }
                content[slot] = itemStack
            }
        }

        inventory.contents = content
    }

    fun createCloseButton(slot: Int) {
        createButton(
            slot,
            Material.BARRIER,
            Component.text("Close", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false),
            ClickTypeGroups.ALL
        ) { it.whoClicked.closeInventory() }
    }

    enum class ClickTypeGroups(vararg val clickTypes: ClickType) {
        ALL(*ClickType.entries.toTypedArray()),
        SHIFT_CLICKS(ClickType.SHIFT_RIGHT, ClickType.SHIFT_LEFT),
        RIGHT_CLICKS(ClickType.RIGHT, ClickType.SHIFT_RIGHT),
        LEFT_CLICKS(ClickType.LEFT, ClickType.SHIFT_LEFT);
    }

}
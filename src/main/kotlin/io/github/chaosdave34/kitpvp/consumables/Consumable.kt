package io.github.chaosdave34.kitpvp.consumables

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class Consumable(val id: String, val name: String, private val startAmount: Int, val rewardAmount: Int, val maxAmount: Int, function: Function0<ItemStack>) {
    private val itemStack: ItemStack = function.invoke()

    fun getItem(amount: Int = 1): ItemStack {
        val items = itemStack.clone()
        items.amount = startAmount
        items.amount = amount
        items.editMeta {
            it.displayName(Component.text(name, NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))
            it.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
            it.lore(
                listOf(
                    Component.text("Start with: ", NamedTextColor.DARK_GRAY)
                        .append(Component.text(startAmount, NamedTextColor.AQUA))
                        .decoration(TextDecoration.ITALIC, false),
                    Component.text("Per kill: ", NamedTextColor.DARK_GRAY)
                        .append(Component.text(rewardAmount, NamedTextColor.AQUA))
                        .decoration(TextDecoration.ITALIC, false),
                    Component.text("Max amount: ", NamedTextColor.DARK_GRAY)
                        .append(Component.text(maxAmount, NamedTextColor.AQUA))
                        .decoration(TextDecoration.ITALIC, false)
                )
            )
        }
        return items
    }

    fun getStart() = getItem(startAmount)
}
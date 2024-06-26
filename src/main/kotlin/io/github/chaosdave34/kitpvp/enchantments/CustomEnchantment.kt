package io.github.chaosdave34.kitpvp.enchantments

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack


abstract class CustomEnchantment(val id: String, val name: String) : Listener {

    fun getDisplayName(level: Int): Component {
        return Component.text(name, NamedTextColor.BLUE)
            .append(Component.space())
            .append(Component.translatable("enchantment.level.$level"))
    }

    fun ItemStack.containsThisEnchantment(): Boolean {
        if (this.type == Material.AIR) return false
        return EnchantmentHandler.containsEnchantment(this, id)
    }

    fun ItemStack.getEnchantmentLevel(): Int {
        return EnchantmentHandler.getEnchantmentLevel(this, id)
    }
}
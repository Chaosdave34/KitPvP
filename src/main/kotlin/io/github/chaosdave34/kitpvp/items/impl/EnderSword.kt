package io.github.chaosdave34.kitpvp.items.impl

import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class EnderSword : CustomItem(Material.STONE_SWORD, "ender_sword", "Ender Sword") {
    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.SHARPNESS, 3)
        itemStack.setCustomModelData(1)
    }
}
package io.github.chaosdave34.kitpvp.items.impl.engineer

import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class EngineersSword : CustomItem(Material.STONE_SWORD, "engineers_sword", "Engineer's Sword") {
    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.SHARPNESS, 2)
    }
}
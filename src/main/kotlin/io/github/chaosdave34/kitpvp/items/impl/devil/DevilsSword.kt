package io.github.chaosdave34.kitpvp.items.impl.devil

import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class DevilsSword : CustomItem(Material.IRON_SWORD, "devils_sword", "Devil's Sword") {
    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1)
        itemStack.setCustomModelData(1)
    }
}
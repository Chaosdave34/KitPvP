package io.github.chaosdave34.kitpvp.items.impl.creeper

import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class CreeperLeggings : CustomItem(Material.LEATHER_LEGGINGS, "creeper_leggings", "Creeper Leggings") {
    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.PROTECTION, 2)
        itemStack.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 4)

        itemStack.setLeatherArmorColor(Color.LIME)
    }
}
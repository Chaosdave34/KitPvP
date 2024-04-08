package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class KnightKit : ElytraKit("elytra_knight", "Knight", Material.IRON_SWORD) {

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack(Material.BOW)
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1)
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3)

        val stoneSword = ItemStack(Material.IRON_SWORD)
        stoneSword.addEnchantment(Enchantment.DAMAGE_ALL, 1)

        return arrayOf(
            bow,
            stoneSword,
        )
    }
}
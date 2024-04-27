package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class PyroKit : ElytraKit("elytra_pyro", "Pyro", Material.FLINT_AND_STEEL) {

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack(Material.BOW)
        bow.addEnchantment(Enchantment.INFINITY, 1)
        bow.addEnchantment(Enchantment.POWER, 2)
        bow.addEnchantment(Enchantment.FLAME, 1)

        return arrayOf(
            bow,
        )
    }
}
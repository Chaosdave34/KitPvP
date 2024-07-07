package io.github.chaosdave34.kitpvp.elytrakits.impl

import io.github.chaosdave34.kitpvp.elytrakits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class TankKit : ElytraKit("elytra_tank", "Tank", Material.IRON_CHESTPLATE) {

    override fun getMaxHealth(): Double = 30.0

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack.of(Material.BOW)
        bow.addEnchantment(Enchantment.INFINITY, 1)
        bow.addEnchantment(Enchantment.POWER, 1)

        return arrayOf(
            bow,
        )
    }
}
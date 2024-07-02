package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class SniperKit : ElytraKit("elytra_sniper", "Sniper", Material.BOW) {

    override fun getMaxHealth(): Double = 10.0

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack.of(Material.BOW)
        bow.addEnchantment(Enchantment.INFINITY, 1)
        bow.addEnchantment(Enchantment.POWER, 5)
        bow.addEnchantment(Enchantment.PUNCH, 1)

        return arrayOf(
            bow,
        )
    }
}
package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class SniperKit : ElytraKit("elytra_sniper", "Sniper", Material.BOW) {

    override fun getMaxHealth(): Double = 10.0

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack(Material.BOW)
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1)
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5)
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1)

        return arrayOf(
            bow,
        )
    }
}
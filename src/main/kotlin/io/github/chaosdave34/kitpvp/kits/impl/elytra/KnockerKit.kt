package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class KnockerKit : ElytraKit("elytra_knocker", "Knocker", Material.STICK) {

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack.of(Material.BOW)
        bow.addEnchantment(Enchantment.INFINITY, 1)
        bow.addEnchantment(Enchantment.POWER, 3)
        bow.addUnsafeEnchantment(Enchantment.PUNCH, 10)
        bow.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10)

        return arrayOf(
            bow,
        )
    }
}
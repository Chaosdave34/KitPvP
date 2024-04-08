package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class KnockerKit : ElytraKit("elytra_knocker", "Knocker", Material.STICK) {

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack(Material.BOW)
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1)
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3)
        bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 10)
        bow.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10)

        return arrayOf(
            bow,
        )
    }
}
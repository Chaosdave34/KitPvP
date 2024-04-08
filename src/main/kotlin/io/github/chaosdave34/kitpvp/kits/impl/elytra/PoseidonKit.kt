package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class PoseidonKit : ElytraKit("elytra_poseidon", "Poseidon", Material.TRIDENT) {

    override fun getInventoryContent(): Array<ItemStack?> {
        val trident = ItemStack(Material.TRIDENT)
        trident.addEnchantment(Enchantment.LOYALTY, 3)

        val bogen = ItemStack(Material.BOW)
        bogen.addEnchantment(Enchantment.ARROW_DAMAGE, 1)
        bogen.addEnchantment(Enchantment.ARROW_INFINITE, 1)

        return arrayOf(
            trident,
            bogen,
        )
    }
}
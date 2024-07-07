package io.github.chaosdave34.kitpvp.elytrakits.impl

import io.github.chaosdave34.kitpvp.elytrakits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class PoseidonKit : ElytraKit("elytra_poseidon", "Poseidon", Material.TRIDENT) {

    override fun getInventoryContent(): Array<ItemStack?> {
        val trident = ItemStack.of(Material.TRIDENT)
        trident.addEnchantment(Enchantment.LOYALTY, 3)

        val bogen = ItemStack.of(Material.BOW)
        bogen.addEnchantment(Enchantment.POWER, 1)
        bogen.addEnchantment(Enchantment.INFINITY, 1)

        return arrayOf(
            trident,
            bogen,
        )
    }
}
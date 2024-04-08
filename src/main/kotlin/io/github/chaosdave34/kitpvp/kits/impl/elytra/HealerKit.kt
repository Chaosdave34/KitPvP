package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class HealerKit : ElytraKit("elytra_healer", "Healer", Material.GOLDEN_APPLE) {

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack(Material.BOW)
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1)
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 2)

        return arrayOf(
            bow,
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.GOLDEN_APPLE, 2)
        )
    }
}
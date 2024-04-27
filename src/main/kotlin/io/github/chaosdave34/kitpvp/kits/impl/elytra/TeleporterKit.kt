package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class TeleporterKit : ElytraKit("elytra_teleporter", "Teleporter", Material.ENDER_PEARL) {
    override fun getInventoryContent(): Array<ItemStack?> {

        val bow = ItemStack(Material.BOW)
        bow.addEnchantment(Enchantment.INFINITY, 1)
        bow.addEnchantment(Enchantment.POWER, 3)

        return arrayOf(
            bow,
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.ENDER_PEARL, 5)
        )
    }
}
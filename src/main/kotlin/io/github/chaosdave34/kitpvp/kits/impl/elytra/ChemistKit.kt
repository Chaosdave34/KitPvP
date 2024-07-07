package io.github.chaosdave34.kitpvp.kits.impl.elytra

import io.github.chaosdave34.kitpvp.kits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

class ChemistKit : ElytraKit("elytra_chemist", "Chemist", Material.POTION) {

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack.of(Material.BOW)
        bow.addEnchantment(Enchantment.INFINITY, 1)
        bow.addEnchantment(Enchantment.POWER, 2)

        return arrayOf(
            bow,
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        val potion = ItemStack.of(Material.SPLASH_POTION, 5)
        potion.editMeta(PotionMeta::class.java) { potionMeta: PotionMeta -> potionMeta.basePotionType = PotionType.STRONG_HARMING }

        return arrayOf(
            potion,
        )
    }
}
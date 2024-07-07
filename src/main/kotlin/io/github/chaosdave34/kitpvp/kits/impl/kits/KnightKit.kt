package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.kits.Kit
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import io.github.chaosdave34.kitpvp.ultimates.UltimateHandler
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class KnightKit : Kit("knight", "Knight") {

    override fun getHeadContent(): ItemStack = ItemStack.of(Material.IRON_HELMET)

    override fun getChestContent(): ItemStack = ItemStack.of(Material.IRON_CHESTPLATE)

    override fun getLegsContent(): ItemStack = ItemStack.of(Material.IRON_LEGGINGS)

    override fun getFeetContent(): ItemStack = ItemStack.of(Material.IRON_BOOTS)

    override fun getOffhandContent(): ItemStack = ItemStack.of(Material.SHIELD)

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack.of(Material.BOW)
        bow.addEnchantment(Enchantment.INFINITY, 1)

        return arrayOf(
            ItemStack.of(Material.DIAMOND_SWORD),
            ItemStack.of(Material.IRON_AXE),
            bow,
            ItemStack.of(Material.ARROW),
            ItemStack.of(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack.of(Material.GOLDEN_APPLE),
            ItemStack.of(Material.COBBLESTONE, 32),
        )
    }

    override fun getUltimate(): Ultimate = UltimateHandler.DASH
}
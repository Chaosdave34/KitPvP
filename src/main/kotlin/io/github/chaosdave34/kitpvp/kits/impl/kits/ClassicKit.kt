package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack as ItemStack

class ClassicKit : Kit("classic", "Classic") {

    override fun getHeadContent(): ItemStack = ItemStack(Material.IRON_HELMET)

    override fun getChestContent(): ItemStack = ItemStack(Material.IRON_CHESTPLATE)

    override fun getLegsContent(): ItemStack = ItemStack(Material.IRON_LEGGINGS)

    override fun getFeetContent(): ItemStack = ItemStack(Material.IRON_BOOTS)

    override fun getOffhandContent(): ItemStack = ItemStack(Material.SHIELD)

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack(Material.BOW)
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1)

        return arrayOf(
            ItemStack(Material.DIAMOND_SWORD),
            ItemStack(Material.IRON_AXE),
            bow,
            ItemStack(Material.ARROW),
            ItemStack(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.GOLDEN_APPLE),
            ItemStack(Material.COBBLESTONE, 32),
        )
    }
}
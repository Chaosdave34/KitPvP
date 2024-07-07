package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import io.github.chaosdave34.kitpvp.ultimates.UltimateHandler
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class ZeusKit : Kit("zeus", "Zeus") {

    override fun getHeadContent(): ItemStack {
        val goldenHelmet = ItemStack.of(Material.GOLDEN_HELMET)
        goldenHelmet.addEnchantment(Enchantment.PROTECTION, 2)
        return goldenHelmet
    }

    override fun getChestContent(): ItemStack {
        val goldenChestplate = ItemStack.of(Material.GOLDEN_CHESTPLATE)
        goldenChestplate.addEnchantment(Enchantment.PROTECTION, 2)
        return goldenChestplate
    }

    override fun getLegsContent(): ItemStack {
        val goldenLeggings = ItemStack.of(Material.GOLDEN_LEGGINGS)
        goldenLeggings.addEnchantment(Enchantment.PROTECTION, 2)
        return goldenLeggings
    }

    override fun getFeetContent(): ItemStack {
        val goldenBoots = ItemStack.of(Material.GOLDEN_BOOTS)
        goldenBoots.addEnchantment(Enchantment.PROTECTION, 2)
        return goldenBoots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        val sword = ItemStack.of(Material.GOLDEN_SWORD)
        sword.addEnchantment(Enchantment.SHARPNESS, 2)

        return arrayOf(
            sword,
            CustomItemHandler.LIGHTNING_WAND.build(),
            ItemStack.of(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack.of(Material.GOLDEN_APPLE),
            ItemStack.of(Material.GOLD_BLOCK, 32),
        )
    }

    override fun getUltimate(): Ultimate = UltimateHandler.RAGE
}
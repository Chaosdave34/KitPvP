package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import io.github.chaosdave34.kitpvp.ultimates.UltimateHandler
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class PoseidonKit : Kit("poseidon", "Poseidon") {

    override fun getHeadContent(): ItemStack {
        val turtleHelmet = ItemStack.of(Material.TURTLE_HELMET)
        turtleHelmet.addEnchantment(Enchantment.PROTECTION, 2)
        turtleHelmet.addEnchantment(Enchantment.RESPIRATION, 3)
        turtleHelmet.addEnchantment(Enchantment.AQUA_AFFINITY, 1)
        return turtleHelmet
    }

    override fun getChestContent(): ItemStack {
        val chestplate = ItemStack.of(Material.LEATHER_CHESTPLATE)
        chestplate.addEnchantment(Enchantment.PROTECTION, 2)
        chestplate.setLeatherArmorColor(Color.AQUA)
        return chestplate
    }

    override fun getLegsContent(): ItemStack {
        val leggings = ItemStack.of(Material.LEATHER_LEGGINGS)
        leggings.addEnchantment(Enchantment.PROTECTION, 2)
        leggings.setLeatherArmorColor(Color.AQUA)
        return leggings
    }

    override fun getFeetContent(): ItemStack {
        val boots = ItemStack.of(Material.LEATHER_BOOTS)
        boots.addEnchantment(Enchantment.PROTECTION, 2)
        boots.addEnchantment(Enchantment.DEPTH_STRIDER, 3)
        boots.setLeatherArmorColor(Color.AQUA)
        return boots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        val trident = ItemStack.of(Material.TRIDENT)
        trident.addUnsafeEnchantment(Enchantment.LOYALTY, 10)
        trident.addUnsafeEnchantment(Enchantment.CHANNELING, 5)

        val riptideTrident = ItemStack.of(Material.TRIDENT)
        riptideTrident.addEnchantment(Enchantment.RIPTIDE, 3)

        return arrayOf(
            trident,
            riptideTrident,
            CustomItemHandler.WATER_BURST.build(),
            ItemStack.of(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack.of(Material.GOLDEN_APPLE),
            ItemStack.of(Material.PRISMARINE, 32),
        )
    }

    override fun getUltimate(): Ultimate = UltimateHandler.STORM
}
package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import io.github.chaosdave34.kitpvp.ultimates.UltimateHandler
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class SpaceSoldierKit : Kit("space soldier", "Space Soldier") {

    override fun getMovementSpeed(): Double = 0.15

    override fun getHeadContent(): ItemStack {
        val helmet = ItemStack.of(Material.BEACON)
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION, 1)
        return helmet
    }

    override fun getChestContent(): ItemStack {
        val chestplate = ItemStack.of(Material.LEATHER_CHESTPLATE)
        chestplate.addEnchantment(Enchantment.PROTECTION, 1)
        chestplate.setLeatherArmorColor(Color.WHITE)
        return chestplate
    }

    override fun getLegsContent(): ItemStack {
        val leggings = ItemStack.of(Material.LEATHER_LEGGINGS)
        leggings.addEnchantment(Enchantment.PROTECTION, 1)
        leggings.setLeatherArmorColor(Color.WHITE)
        return leggings
    }

    override fun getFeetContent(): ItemStack {
        val boots = ItemStack.of(Material.LEATHER_BOOTS)
        boots.addEnchantment(Enchantment.PROTECTION, 1)
        boots.setLeatherArmorColor(Color.WHITE)
        return boots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        val knockBackStick = ItemStack.of(Material.STICK)
        knockBackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2)

        return arrayOf(
            ItemStack.of(Material.DIAMOND_SWORD),
            knockBackStick,
            CustomItemHandler.AIRSTRIKE_REQUESTER.build(),
            CustomItemHandler.BLACK_HOLE_GENERATOR.build(),
            ItemStack.of(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack.of(Material.GOLDEN_APPLE),
            ItemStack.of(Material.COBBLESTONE, 32),
        )
    }

    override fun getUltimate(): Ultimate = UltimateHandler.DARKNESS
}
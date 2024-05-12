package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class SpaceSoldierKit : Kit("space soldier", "Space Soldier") {

    override fun getMovementSpeed(): Double = 0.15

    override fun getHeadContent(): ItemStack {
        val helmet = ItemStack(Material.BEACON)
        helmet.addUnsafeEnchantment(Enchantment.PROTECTION, 1)
        return helmet
    }

    override fun getChestContent(): ItemStack {
        val chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
        chestplate.addEnchantment(Enchantment.PROTECTION, 1)
        chestplate.setLeatherArmorColor(Color.WHITE)
        return chestplate
    }

    override fun getLegsContent(): ItemStack {
        val leggings = ItemStack(Material.LEATHER_LEGGINGS)
        leggings.addEnchantment(Enchantment.PROTECTION, 1)
        leggings.setLeatherArmorColor(Color.WHITE)
        return leggings
    }

    override fun getFeetContent(): ItemStack {
        val boots = ItemStack(Material.LEATHER_BOOTS)
        boots.addEnchantment(Enchantment.PROTECTION, 1)
        boots.setLeatherArmorColor(Color.WHITE)
        return boots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        val knockBackStick = ItemStack(Material.STICK)
        knockBackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2)

        return arrayOf(
            CustomItemHandler.SPACE_SWORD.build(),
            knockBackStick,
            CustomItemHandler.AIRSTRIKE_REQUESTER.build(),
            CustomItemHandler.BLACK_HOLE_GENERATOR.build(),
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
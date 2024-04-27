package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.companions.Companion
import io.github.chaosdave34.kitpvp.companions.CompanionHandler
import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class DevilKit : Kit("devil", "Devil") {
    override fun getHeadContent(): ItemStack {

        val helmet = ItemStack(Material.LEATHER_HELMET)
        helmet.addEnchantment(Enchantment.PROTECTION, 2)
        helmet.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 4)
        helmet.setLeatherArmorColor(Color.RED)
        return helmet
    }

    override fun getChestContent(): ItemStack {
        val chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
        chestplate.addEnchantment(Enchantment.PROTECTION, 2)
        chestplate.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 4)
        chestplate.setLeatherArmorColor(Color.RED)
        return chestplate
    }

    override fun getLegsContent(): ItemStack {
        val leggings = ItemStack(Material.LEATHER_LEGGINGS)
        leggings.addEnchantment(Enchantment.PROTECTION, 2)
        leggings.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 4)
        leggings.setLeatherArmorColor(Color.RED)
        return leggings
    }

    override fun getFeetContent(): ItemStack {
        val boots = ItemStack(Material.LEATHER_BOOTS)
        boots.addEnchantment(Enchantment.PROTECTION, 2)
        boots.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 4)
        boots.setLeatherArmorColor(Color.RED)
        return boots
    }
    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack(Material.BOW)
        bow.addEnchantment(Enchantment.INFINITY, 1)
        bow.addEnchantment(Enchantment.FLAME, 1)
        bow.setCustomModelData(1)

        return arrayOf(
            CustomItemHandler.DEVILS_SWORD.build(),
            bow,
            ItemStack(Material.ARROW),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.GOLDEN_APPLE),
            ItemStack(Material.NETHERRACK, 32),
            ItemStack(Material.TWISTING_VINES, 2)
        )
    }

    override fun getCompanion(): Companion = CompanionHandler.ZOMBIFIED_PIGLIN
}
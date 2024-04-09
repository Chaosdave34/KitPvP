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

        val leatherHelmet = ItemStack(Material.LEATHER_HELMET)
        leatherHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4)
        leatherHelmet.setLeatherArmorColor(Color.RED)
        return leatherHelmet
    }

    override fun getChestContent(): ItemStack {
        val leatherChestplate = ItemStack(Material.LEATHER_CHESTPLATE)
        leatherChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4)
        leatherChestplate.setLeatherArmorColor(Color.RED)
        return leatherChestplate
    }

    override fun getLegsContent(): ItemStack {
        val leatherLeggings = ItemStack(Material.LEATHER_LEGGINGS)
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4)
        leatherLeggings.setLeatherArmorColor(Color.RED)
        return leatherLeggings
    }

    override fun getFeetContent(): ItemStack {
        val leatherBoots = ItemStack(Material.LEATHER_BOOTS)
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherBoots.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4)
        leatherBoots.setLeatherArmorColor(Color.RED)
        return leatherBoots
    }
    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack(Material.BOW)
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1)
        bow.addEnchantment(Enchantment.ARROW_FIRE, 1)
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
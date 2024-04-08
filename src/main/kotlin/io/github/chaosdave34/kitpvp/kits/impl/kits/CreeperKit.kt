package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class CreeperKit : Kit("creeper", "Creeper") {

    override fun getHeadContent(): ItemStack {
        val creeperHead = ItemStack(Material.CREEPER_HEAD)
        creeperHead.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        creeperHead.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4)
        return creeperHead
    }

    override fun getChestContent(): ItemStack {
        val leatherChestplate = ItemStack(Material.LEATHER_CHESTPLATE)
        leatherChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4)
        setLeatherArmorColor(leatherChestplate, Color.LIME)
        return leatherChestplate
    }

    override fun getLegsContent(): ItemStack = CustomItemHandler.CREEPER_LEGGINGS.build()

    override fun getFeetContent(): ItemStack {
        val leatherBoots = ItemStack(Material.LEATHER_BOOTS)
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherBoots.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4)
        setLeatherArmorColor(leatherBoots, Color.LIME)
        return leatherBoots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.CREEPER_SWORD.build(),
            CustomItemHandler.FIREBALL.build(),
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
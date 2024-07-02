package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import io.github.chaosdave34.kitpvp.ultimates.UltimateHandler
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class CreeperKit : Kit("creeper", "Creeper") {

    override fun getHeadContent(): ItemStack {
        val creeperHead = ItemStack.of(Material.CREEPER_HEAD)
        creeperHead.addUnsafeEnchantment(Enchantment.PROTECTION, 2)
        creeperHead.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 4)
        return creeperHead
    }

    override fun getChestContent(): ItemStack {
        val chestplate = ItemStack.of(Material.LEATHER_CHESTPLATE)
        chestplate.addEnchantment(Enchantment.PROTECTION, 2)
        chestplate.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 4)
        chestplate.setLeatherArmorColor(Color.LIME)
        return chestplate
    }

    override fun getLegsContent(): ItemStack = CustomItemHandler.CREEPER_LEGGINGS.build()

    override fun getFeetContent(): ItemStack {
        val boots = ItemStack.of(Material.LEATHER_BOOTS)
        boots.addEnchantment(Enchantment.PROTECTION, 2)
        boots.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 4)
        boots.setLeatherArmorColor(Color.LIME)
        return boots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        val sword = ItemStack.of(Material.IRON_SWORD)
        sword.setCustomModelData(2)
        
        return arrayOf(
            sword,
            CustomItemHandler.FIREBALL.build(),
            ItemStack.of(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack.of(Material.GOLDEN_APPLE),
            ItemStack.of(Material.COBBLESTONE, 32),
        )
    }

    override fun getUltimate(): Ultimate = UltimateHandler.CHARGE
}
package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

class VampireKit : Kit("vampire", "Vampire") {

    override fun getHeadContent(): ItemStack {
        val helmet = ItemStack(Material.LEATHER_HELMET)
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        helmet.setLeatherArmorColor(Color.RED)
        return helmet
    }

    override fun getChestContent(): ItemStack {
        val chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        chestplate.setLeatherArmorColor(Color.BLACK)
        return chestplate
    }

    override fun getLegsContent(): ItemStack {
        val leggings = ItemStack(Material.LEATHER_LEGGINGS)
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leggings.setLeatherArmorColor(Color.BLACK)
        return leggings
    }

    override fun getFeetContent(): ItemStack {
        val boots = ItemStack(Material.LEATHER_BOOTS)
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        boots.setLeatherArmorColor(Color.BLACK)
        return boots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.VAMPIRE_SWORD.build(),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.RED_NETHER_BRICKS, 32),
        )
    }

    override fun getPotionEffects(): Map<PotionEffectType, Int> {
        return mapOf(Pair(PotionEffectType.NIGHT_VISION, 0))
    }
}

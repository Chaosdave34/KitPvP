package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import io.github.chaosdave34.kitpvp.ultimates.UltimateHandler
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

class ArcherKit : Kit("archer", "Archer") {
    override fun getHeadContent(): ItemStack {
        val helmet = ItemStack.of(Material.LEATHER_HELMET)
        helmet.setLeatherArmorColor(Color.GREEN)
        return helmet
    }

    override fun getChestContent(): ItemStack {
        val chestplate = ItemStack.of(Material.LEATHER_CHESTPLATE)
        chestplate.setLeatherArmorColor(Color.GREEN)
        return chestplate
    }

    override fun getLegsContent(): ItemStack = ItemStack.of(Material.CHAINMAIL_LEGGINGS)

    override fun getFeetContent(): ItemStack = ItemStack.of(Material.CHAINMAIL_BOOTS)

    override fun getInventoryContent(): Array<ItemStack?> {
        val bow = ItemStack.of(Material.BOW)
        bow.addEnchantment(Enchantment.INFINITY, 1)

        val stoneSword = ItemStack.of(Material.STONE_SWORD)
        stoneSword.addEnchantment(Enchantment.KNOCKBACK, 2)
        stoneSword.setCustomModelData(2)

        return arrayOf(
            stoneSword,
            bow,
            CustomItemHandler.LONG_BOW.build(),
            CustomItemHandler.LEAP.build(),
            ItemStack.of(Material.ARROW),
            ItemStack.of(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack.of(Material.GOLDEN_APPLE),
            ItemStack.of(Material.COBBLESTONE, 32),
        )
    }

    override fun getPotionEffects(): Map<PotionEffectType, Int> {
        return mapOf(Pair(PotionEffectType.JUMP_BOOST, 2))
    }

    override fun getUltimate(): Ultimate = UltimateHandler.ARROW_RAIN

}
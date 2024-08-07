package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import io.github.chaosdave34.kitpvp.ultimates.UltimateHandler
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class TankKit : Kit("tank", "Tank") {

    override fun getMaxHealth(): Double = 40.0

    override fun getHeadContent(): ItemStack = ItemStack.of(Material.IRON_HELMET)

    override fun getChestContent(): ItemStack = ItemStack.of(Material.IRON_CHESTPLATE)

    override fun getLegsContent(): ItemStack = ItemStack.of(Material.IRON_LEGGINGS)

    override fun getFeetContent(): ItemStack = CustomItemHandler.TANK_BOOTS.build()

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            ItemStack.of(Material.STONE_AXE),
            ItemStack.of(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack.of(Material.GOLDEN_APPLE, 2),
            ItemStack.of(Material.COBBLED_DEEPSLATE, 32),
        )
    }

    override fun getUltimate(): Ultimate = UltimateHandler.TORNADO
}
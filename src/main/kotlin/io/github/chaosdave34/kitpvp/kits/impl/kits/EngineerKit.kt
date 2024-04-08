package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class EngineerKit : Kit("engineer", "Engineer"){

    override fun getHeadContent(): ItemStack = ItemStack(Material.CHAINMAIL_HELMET)

    override fun getChestContent(): ItemStack = ItemStack(Material.CHAINMAIL_CHESTPLATE)

    override fun getLegsContent(): ItemStack = ItemStack(Material.CHAINMAIL_LEGGINGS)

    override fun getFeetContent(): ItemStack = ItemStack(Material.CHAINMAIL_BOOTS)

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.ENGINEERS_SWORD.build(),
            CustomItemHandler.MODULAR_SHIELD.build(),
            CustomItemHandler.TURRET.build(),
            ItemStack(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.COBBLESTONE, 32),
            ItemStack(Material.GOLDEN_APPLE),
        )
    }
}
package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.companions.Companion
import io.github.chaosdave34.kitpvp.companions.CompanionHandler
import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class AssassinKit : Kit("assassin", "Assassin") {

    override fun getHeadContent(): ItemStack = ItemStack(Material.CHAINMAIL_HELMET)

    override fun getChestContent(): ItemStack = ItemStack(Material.IRON_CHESTPLATE)

    override fun getLegsContent(): ItemStack = ItemStack(Material.CHAINMAIL_LEGGINGS)

    override fun getFeetContent(): ItemStack = ItemStack(Material.CHAINMAIL_BOOTS)

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.ASSASSIN_SWORD.build(),
            ItemStack(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.GOLDEN_APPLE),
            ItemStack(Material.COBBLESTONE, 32),
        )
    }

    override fun getCompanion(): Companion = CompanionHandler.ALLAY
}
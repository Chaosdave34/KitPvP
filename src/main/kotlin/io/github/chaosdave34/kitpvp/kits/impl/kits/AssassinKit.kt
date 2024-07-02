package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.companions.Companion
import io.github.chaosdave34.kitpvp.companions.CompanionHandler
import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class AssassinKit : Kit("assassin", "Assassin *") {

    override fun getHeadContent(): ItemStack = ItemStack.of(Material.CHAINMAIL_HELMET)

    override fun getChestContent(): ItemStack = ItemStack.of(Material.IRON_CHESTPLATE)

    override fun getLegsContent(): ItemStack = ItemStack.of(Material.CHAINMAIL_LEGGINGS)

    override fun getFeetContent(): ItemStack = ItemStack.of(Material.CHAINMAIL_BOOTS)

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.ASSASSIN_SWORD.build(),
            ItemStack.of(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack.of(Material.GOLDEN_APPLE),
            ItemStack.of(Material.COBBLESTONE, 32),
        )
    }

    override fun getCompanion(): Companion = CompanionHandler.ALLAY
}
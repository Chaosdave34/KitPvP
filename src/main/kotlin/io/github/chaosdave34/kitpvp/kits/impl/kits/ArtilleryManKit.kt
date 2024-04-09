package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta

class ArtilleryManKit : Kit("artillery_man", "Artillery Man" ) {

    override fun getHeadContent(): ItemStack {
        val leatherHelmet = ItemStack(Material.LEATHER_HELMET)
        leatherHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4)
        leatherHelmet.setLeatherArmorColor(Color.NAVY)
        return leatherHelmet
    }

    override fun getChestContent(): ItemStack = CustomItemHandler.JETPACK.build()

    override fun getLegsContent(): ItemStack {
        val leatherLeggings = ItemStack(Material.LEATHER_LEGGINGS)
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4)
        leatherLeggings.setLeatherArmorColor(Color.NAVY)
        return leatherLeggings
    }

    override fun getFeetContent(): ItemStack {
        val leatherBoots = ItemStack(Material.LEATHER_BOOTS)
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherBoots.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4)
        leatherBoots.addEnchantment(Enchantment.PROTECTION_FALL, 4)
        leatherBoots.setLeatherArmorColor(Color.NAVY)
        return leatherBoots
    }

    override fun getOffhandContent(): ItemStack {
        val rocket = ItemStack(Material.FIREWORK_ROCKET, 3)
        rocket.editMeta(FireworkMeta::class.java) { fireworkMeta: FireworkMeta -> fireworkMeta.power = 5 }
        return rocket
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.ROCKET_LAUNCHER.build(),
            ItemStack(Material.STONE_SWORD),
            ItemStack(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.GOLDEN_APPLE),
            ItemStack(Material.COBBLESTONE, 32),
        )
    }

    @EventHandler
    fun onRocketLaunch(e: PlayerInteractEvent) {
        val player = e.player
        if (player.isKitActive()) {
            if (e.material == Material.FIREWORK_ROCKET) {
                e.isCancelled = true
            }
        }
    }
}
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

class ArtilleryManKit : Kit("artillery_man", "Artillery Man *" ) {

    override fun getHeadContent(): ItemStack {
        val helmet = ItemStack(Material.LEATHER_HELMET)
        helmet.addEnchantment(Enchantment.PROTECTION, 2)
        helmet.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 4)
        helmet.setLeatherArmorColor(Color.NAVY)
        return helmet
    }

    override fun getChestContent(): ItemStack = CustomItemHandler.JETPACK.build()

    override fun getLegsContent(): ItemStack {
        val leggings = ItemStack(Material.LEATHER_LEGGINGS)
        leggings.addEnchantment(Enchantment.PROTECTION, 2)
        leggings.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 4)
        leggings.setLeatherArmorColor(Color.NAVY)
        return leggings
    }

    override fun getFeetContent(): ItemStack {
        val boots = ItemStack(Material.LEATHER_BOOTS)
        boots.addEnchantment(Enchantment.PROTECTION, 2)
        boots.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 4)
        boots.addEnchantment(Enchantment.FEATHER_FALLING, 4)
        boots.setLeatherArmorColor(Color.NAVY)
        return boots
    }

    override fun getOffhandContent(): ItemStack {
        val rocket = ItemStack(Material.FIREWORK_ROCKET, 3)
        rocket.editMeta(FireworkMeta::class.java) { fireworkMeta: FireworkMeta ->
            fireworkMeta.power = 5
        }
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
    fun onRocketLaunch(event: PlayerInteractEvent) {
        val player = event.player
        if (player.isKitActive()) {
            if (event.material == Material.FIREWORK_ROCKET) {
                event.isCancelled = true
            }
        }
    }
}
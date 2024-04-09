package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack

class EndermanKit : Kit("enderman", "Enderman") {

    override fun getHeadContent(): ItemStack {
        val leatherHelmet = ItemStack(Material.LEATHER_HELMET)
        leatherHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10)
        leatherHelmet.setLeatherArmorColor(Color.BLACK)
        return leatherHelmet
    }

    override fun getChestContent(): ItemStack {
        val leatherChestplate = ItemStack(Material.LEATHER_CHESTPLATE)
        leatherChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10)
        leatherChestplate.setLeatherArmorColor(Color.BLACK)
        return leatherChestplate
    }

    override fun getLegsContent(): ItemStack {
        val leatherLeggings = ItemStack(Material.LEATHER_LEGGINGS)
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10)
        leatherLeggings.setLeatherArmorColor(Color.BLACK)
        return leatherLeggings
    }

    override fun getFeetContent(): ItemStack {
        val leatherBoots = ItemStack(Material.LEATHER_BOOTS)
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherBoots.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 10)
        leatherBoots.setLeatherArmorColor(Color.BLACK)
        return leatherBoots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.ENDER_SWORD.build(),
            CustomItemHandler.DRAGONS_CHARGE.build(),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.GOLDEN_APPLE),
            ItemStack(Material.END_STONE, 32),
            ItemStack(Material.ENDER_PEARL, 3),
        )
    }

    @EventHandler
    fun onEnderPearlDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player is Player) {
            if (player.isKitActive()) {
                if (event.cause == EntityDamageEvent.DamageCause.PROJECTILE && event.damageSource.damageType === DamageType.FALL) { // Test for ender pearl damage
                    event.isCancelled = true
                }
            }
        }
    }

    @EventHandler
    fun inWater(e: PlayerMoveEvent) {
        val player = e.player
        if (player.isKitActive()) {
            if (e.to.block.type == Material.WATER) {
                player.damage(1.0, DamageSource.builder(DamageType.DROWN).build())
            }
        }
    }
}
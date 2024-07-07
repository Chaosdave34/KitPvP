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

class EndermanKit : Kit("enderman", "Enderman *") {

    override fun getHeadContent(): ItemStack {
        val helmet = ItemStack.of(Material.LEATHER_HELMET)
        helmet.addEnchantment(Enchantment.PROTECTION, 2)
        helmet.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 10)
        helmet.setLeatherArmorColor(Color.BLACK)
        return helmet
    }

    override fun getChestContent(): ItemStack {
        val chestplate = ItemStack.of(Material.LEATHER_CHESTPLATE)
        chestplate.addEnchantment(Enchantment.PROTECTION, 2)
        chestplate.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 10)
        chestplate.setLeatherArmorColor(Color.BLACK)
        return chestplate
    }

    override fun getLegsContent(): ItemStack {
        val leggings = ItemStack.of(Material.LEATHER_LEGGINGS)
        leggings.addEnchantment(Enchantment.PROTECTION, 2)
        leggings.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 10)
        leggings.setLeatherArmorColor(Color.BLACK)
        return leggings
    }

    override fun getFeetContent(): ItemStack {
        val boots = ItemStack.of(Material.LEATHER_BOOTS)
        boots.addEnchantment(Enchantment.PROTECTION, 2)
        boots.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 10)
        boots.setLeatherArmorColor(Color.BLACK)
        return boots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.ENDER_SWORD.build(),
            CustomItemHandler.DRAGONS_CHARGE.build(),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack.of(Material.GOLDEN_APPLE),
            ItemStack.of(Material.END_STONE, 32),
            ItemStack.of(Material.ENDER_PEARL, 3),
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
    fun inWater(event: PlayerMoveEvent) {
        val player = event.player
        if (player.isKitActive()) {
            if (event.to.block.type == Material.WATER) {
                player.damage(1.0, DamageSource.builder(DamageType.DROWN).build())
            }
        }
    }
}
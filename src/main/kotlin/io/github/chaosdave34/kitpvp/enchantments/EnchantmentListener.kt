package io.github.chaosdave34.kitpvp.enchantments

import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.abs
import kotlin.math.min

class EnchantmentListener : Listener {
    // Backstab
    @EventHandler
    fun onDealDamage(event: EntityDealDamageEvent) {
        val player = event.damager
        if (player is Player) {
            val item: ItemStack = player.inventory.itemInMainHand
            if (item.containsEnchantment(CustomEnchantments.BACKSTAB)) {
                if (abs(player.getLocation().getDirection().angle(event.target.location.direction) * 180 / Math.PI) < 30) {
                    val level = item.getEnchantmentLevel(CustomEnchantments.BACKSTAB)
                    event.damage *= 1 + (level * 0.25)
                }
            }
        }
    }

    // Freeze
    @EventHandler
    fun onAttack(event: EntityDamageByEntityEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            val player = event.damager
            if (player is Player && event.entity is LivingEntity) {
                val itemStack: ItemStack = player.inventory.itemInMainHand
                if (itemStack.containsEnchantment(CustomEnchantments.FREEZE)) {
                    val level = itemStack.getEnchantmentLevel(CustomEnchantments.FREEZE)
                    player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, level * 20, 1, false, false))
                }
            }
        }
    }

    // Life Steal
    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val player = event.damager
        if (player is Player) {
            val item: ItemStack = player.inventory.itemInMainHand
            if (item.containsEnchantment(CustomEnchantments.LIFE_STEAL)) {
                val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
                val maxHealthValue = maxHealth.value

                val currentHealth: Double = player.health

                val heal = event.damage * item.getEnchantmentLevel(CustomEnchantments.LIFE_STEAL) * 0.1

                player.health = min(currentHealth + heal, maxHealthValue)
            }
        }
    }
}
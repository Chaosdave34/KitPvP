package io.github.chaosdave34.kitpvp.enchantments.impl

import io.github.chaosdave34.kitpvp.enchantments.CustomEnchantment
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.min

class LifeStealEnchantment: CustomEnchantment("life_steal", "Life Steal") {
    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val player = event.damager
        if (player is Player) {
            val item: ItemStack = player.inventory.itemInMainHand
            if (item.containsThisEnchantment()) {
                val maxHealth: AttributeInstance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
                val maxHealthValue = maxHealth.value

                val currentHealth: Double = player.health

                val heal = event.damage * item.getEnchantmentLevel() * 0.1

                player.health = min(currentHealth + heal, maxHealthValue)
            }
        }
    }
}
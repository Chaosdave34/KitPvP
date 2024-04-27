package io.github.chaosdave34.kitpvp.enchantments.impl

import io.github.chaosdave34.kitpvp.enchantments.CustomEnchantment
import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.inventory.ItemStack
import kotlin.math.abs

class BackstabEnchantment : CustomEnchantment("backstab", "Backstab") {
    @EventHandler
    fun onDealDamage(event: EntityDealDamageEvent) {
        val player = event.damager
        if (player is Player) {
            val item: ItemStack = player.inventory.itemInMainHand
            if (item.containsThisEnchantment()) {
                if (abs(player.getLocation().getDirection().angle(event.target.location.direction) * 180 / Math.PI) < 30) {
                    event.damage *= 1 + (item.getEnchantmentLevel() * 0.25)
                }
            }
        }
    }
}
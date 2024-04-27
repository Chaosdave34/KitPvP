package io.github.chaosdave34.kitpvp.enchantments.impl

import io.github.chaosdave34.kitpvp.enchantments.CustomEnchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FreezeEnchantment : CustomEnchantment("freeze", "Freeze") {
    @EventHandler
    fun onAttack(event: EntityDamageByEntityEvent) {
        if (event.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            val player = event.damager
            if (player is Player) {
                val itemStack: ItemStack = player.inventory.itemInMainHand
                if (itemStack.containsThisEnchantment()) {
                    val lvl = itemStack.getEnchantmentLevel()
                    if (event.entity is LivingEntity) {
                        player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, lvl * 20, 1, false, false))
                    }
                }
            }
        }
    }
}
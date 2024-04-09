package io.github.chaosdave34.kitpvp.listener

import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent
import io.github.chaosdave34.kitpvp.events.EntityReceiveDamageByEntityEvent
import org.bukkit.entity.LightningStrike
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class EntityDamageListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityDamage(event: EntityDamageEvent) {
        val damageSource = event.damageSource

        val receiveDamageEvent =
            EntityReceiveDamageByEntityEvent(event.entity, damageSource.causingEntity ?: damageSource.directEntity ?: return, event.damage, event)

        receiveDamageEvent.callEvent()

        event.damage = receiveDamageEvent.damage
        event.isCancelled = receiveDamageEvent.isCancelled

        val dealDamageEvent = EntityDealDamageEvent(receiveDamageEvent.damager, receiveDamageEvent.entity, receiveDamageEvent.damage, receiveDamageEvent.damageEvent)
        dealDamageEvent.callEvent()

        event.damage = dealDamageEvent.damage
        event.isCancelled = dealDamageEvent.isCancelled
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onLightningStrike(e: EntityDamageByEntityEvent) {
        val damager = e.damager
        if (damager is LightningStrike) {
            val causingEntity = damager.causingEntity
            if (causingEntity != null) {
                val receiveDamageEvent = EntityReceiveDamageByEntityEvent(e.entity, causingEntity, e.damage, e)
                receiveDamageEvent.callEvent()

                e.damage = receiveDamageEvent.damage
                e.isCancelled = receiveDamageEvent.isCancelled

                val dealDamageEvent = EntityDealDamageEvent(causingEntity, e.entity, e.damage, e)
                dealDamageEvent.callEvent()

                e.damage = dealDamageEvent.damage
                e.isCancelled = dealDamageEvent.isCancelled
            }
        }
    }
}
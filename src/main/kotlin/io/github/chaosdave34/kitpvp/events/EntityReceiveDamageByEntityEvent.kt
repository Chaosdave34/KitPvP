package io.github.chaosdave34.kitpvp.events

import org.bukkit.entity.Entity
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.entity.EntityDamageEvent

/**
 * Convenience event to get informed when an entity receives damage to get the actual damager
 * [EntityDealDamageEvent]  gets called directly after this event (lower priority)
 */
class EntityReceiveDamageByEntityEvent(
    val entity: Entity,
    val damager: Entity,
    var damage: Double,
    var damageEvent: EntityDamageEvent
) : Event(), Cancellable {
    private var cancelled = damageEvent.isCancelled

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlerList
    }

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }
}
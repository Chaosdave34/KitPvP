package io.github.chaosdave34.kitpvp.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Convenience event to get informed when an entity receives damage to get the actual damager
 * Gets called after {@link EntityDealDamageEvent}
 */
public class EntityReceiveDamageByEntityEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    @Getter
    private final Entity entity;
    @Getter
    private final Entity damager;
    @Getter
    @Setter
    private double damage;
    @Getter
    private final EntityDamageEvent damageEvent;
    private boolean cancelled;

    public EntityReceiveDamageByEntityEvent(Entity entity, Entity damager, double damage, EntityDamageEvent damageEvent) {
        this.entity = entity;
        this.damager = damager;
        this.damage = damage;
        this.damageEvent = damageEvent;
        this.cancelled = damageEvent.isCancelled();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}

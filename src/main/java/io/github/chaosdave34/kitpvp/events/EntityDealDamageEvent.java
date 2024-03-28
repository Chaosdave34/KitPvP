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
 * Convenience event to get informed when an entity deals damage to get the target
 * {@link EntityReceiveDamageByEntityEvent} gets called directly after this event
 */
public class EntityDealDamageEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    @Getter
    private final Entity damager;
    @Getter
    private final Entity target;
    @Getter
    @Setter
    private double damage;
    @Getter
    private final EntityDamageEvent damageEvent;
    private boolean cancelled;

    public EntityDealDamageEvent(Entity damager, Entity target, double damage, EntityDamageEvent damageEvent) {
        this.damager = damager;
        this.target = target;
        this.damage = damage;
        this.damageEvent = damageEvent;
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

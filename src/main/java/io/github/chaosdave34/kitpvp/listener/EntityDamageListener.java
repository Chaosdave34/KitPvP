package io.github.chaosdave34.kitpvp.listener;

import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent;
import io.github.chaosdave34.kitpvp.events.EntityReceiveDamageByEntityEvent;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        DamageSource damageSource = e.getDamageSource();

        EntityDealDamageEvent dealDamageEvent;

        if (damageSource.getCausingEntity() != null) {
            dealDamageEvent = new EntityDealDamageEvent(damageSource.getCausingEntity(), e.getEntity(), e.getDamage(), e);
        } else if (damageSource.getDirectEntity() != null) {
            dealDamageEvent = new EntityDealDamageEvent(damageSource.getDirectEntity(), e.getEntity(), e.getDamage(), e);
        } else {
            return;
        }

        dealDamageEvent.callEvent();

        e.setDamage(dealDamageEvent.getDamage());
        e.setCancelled(dealDamageEvent.isCancelled());

        EntityReceiveDamageByEntityEvent receiveDamageEvent = new EntityReceiveDamageByEntityEvent(dealDamageEvent.getTarget(),  dealDamageEvent.getDamager(), dealDamageEvent.getDamage(), dealDamageEvent.getDamageEvent());
        receiveDamageEvent.callEvent();

        e.setDamage(receiveDamageEvent.getDamage());
        e.setCancelled(receiveDamageEvent.isCancelled());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLightningStrike(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof LightningStrike lightningStrike) {
            if (lightningStrike.getCausingEntity() != null) {
                EntityDealDamageEvent dealDamageEvent = new EntityDealDamageEvent(lightningStrike.getCausingEntity(), e.getEntity(), e.getDamage(), e);
                dealDamageEvent.callEvent();

                e.setDamage(dealDamageEvent.getDamage());
                e.setCancelled(dealDamageEvent.isCancelled());

                EntityReceiveDamageByEntityEvent receiveDamageEvent = new EntityReceiveDamageByEntityEvent(e.getEntity(), lightningStrike.getCausingEntity(), e.getDamage(), e);
                receiveDamageEvent.callEvent();

                e.setDamage(receiveDamageEvent.getDamage());
                e.setCancelled(receiveDamageEvent.isCancelled());
            }
        }
    }
}

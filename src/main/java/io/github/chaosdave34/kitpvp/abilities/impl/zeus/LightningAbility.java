package io.github.chaosdave34.kitpvp.abilities.impl.zeus;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightningAbility extends Ability {
    public LightningAbility() {
        super("lightning", "Lightning", Type.RIGHT_CLICK, 3);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Strikes a lightning bold",
                "at the Enemy you are looking at",
                "in a 10 block radius."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        Entity target = p.getTargetEntity(10);
        if (target instanceof LivingEntity livingEntity) {

            if (target instanceof Player player) {
                if (ExtendedPlayer.from(player).inSpawn())
                    return false;
            }

            Location targetLocation = livingEntity.getLocation();
            targetLocation.getWorld().spawnEntity(targetLocation, EntityType.LIGHTNING, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
                LightningStrike lightningStrike = ((LightningStrike) entity);
                lightningStrike.setCausingPlayer(p);
                lightningStrike.setFlashCount(1);

                entity.setMetadata("ability", new FixedMetadataValue(KitPvp.INSTANCE, getId()));
            });
            return true;
        }
        return false;
    }

    @EventHandler
    public void onLightningImpact(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p && e.getDamager() instanceof LightningStrike lightningStrike) {
            if (lightningStrike.hasMetadata("ability")) {
                if (getId().equals(lightningStrike.getMetadata("ability").get(0).value())) {
                    if (p.equals(lightningStrike.getCausingPlayer())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}

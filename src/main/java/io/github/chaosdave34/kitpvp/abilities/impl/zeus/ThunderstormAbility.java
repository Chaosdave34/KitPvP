package io.github.chaosdave34.kitpvp.abilities.impl.zeus;

import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ThunderstormAbility extends Ability {

    public ThunderstormAbility() {
        super("thunderstorm", "Thunderstorm", Type.LEFT_CLICK, 30);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Summons Lightning around you in a 5 block radius.");
    }

    @Override
    public boolean onAbility(Player p) {
        Location location = p.getLocation();

        double[] offsets_values = new double[]{-3.5, -1.5, 1.5, 3.5};

        List<Vector> offsets = new ArrayList<>();

        for (double x : offsets_values) {
            for (double z : offsets_values) {
                if (x == 0 && z == 0) continue;
                offsets.add(new Vector(x, -1, z));
            }
        }

        Collections.shuffle(offsets);

        new BukkitRunnable() {
            final Iterator<Vector> iterator = offsets.iterator();

            @Override
            public void run() {
                if (iterator.hasNext()) {
                    Location targetLocation = location.clone().add(iterator.next());

                    location.getWorld().spawnEntity(targetLocation, EntityType.LIGHTNING, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
                        LightningStrike lightningStrike = ((LightningStrike) entity);
                        lightningStrike.setCausingPlayer(p);
                        lightningStrike.setFlashCount(1);

                        entity.setMetadata("ability", new FixedMetadataValue(KitPvp.INSTANCE, getId()));
                    });
                } else this.cancel();
            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, 2);

        return true;
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

    @EventHandler
    public void onSetFire(BlockIgniteEvent e) {
        if (e.getIgnitingEntity() instanceof LightningStrike lightningStrike) {
            if (lightningStrike.hasMetadata("ability")) {
                if (getId().equals(lightningStrike.getMetadata("ability").get(0).value())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}

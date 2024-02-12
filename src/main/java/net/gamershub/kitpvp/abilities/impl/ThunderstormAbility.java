package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        super("thunderstorm", "Thunderstorm", AbilityType.LEFT_CLICK, 30);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        List<Component> description = new ArrayList<>();
        description.add(Component.text("Summons Lightning around you", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        description.add(Component.text("in a 5 block radius.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        return description;
    }

    @Override
    public boolean onAbility(PlayerInteractEvent e) {
        Location location = e.getPlayer().getLocation();

        int[] offsets_values = new int[]{-5, -3, 0, 3, 5};

        List<Vector> offsets = new ArrayList<>();

        for (int x : offsets_values) {
            for (int z : offsets_values) {
                if (x == 0 && z == 0) continue;
                offsets.add(new Vector(x, 0, z));
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
                        ((LightningStrike) entity).setCausingPlayer(e.getPlayer());
                        entity.setMetadata("ability", new FixedMetadataValue(KitPvpPlugin.INSTANCE, id));
                    });
                } else this.cancel();
            }
        }.runTaskTimer(KitPvpPlugin.INSTANCE, 0, 2);

        return true;
    }

    @EventHandler
    public void onLightningImpact(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p && e.getDamager() instanceof LightningStrike lightningStrike) {
            if (lightningStrike.hasMetadata("ability")) {
                if (id.equals(lightningStrike.getMetadata("ability").get(0).value())) {
                    if (p.equals(lightningStrike.getCausingPlayer())) {
                        e.setCancelled(true);
                    }
                }
            }

        }
    }
}

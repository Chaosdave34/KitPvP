package io.github.chaosdave34.kitpvp.abilities.impl.tank;

import io.github.chaosdave34.ghutils.utils.MathUtils;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SeismicWaveAbility extends Ability {
    public SeismicWaveAbility() {
        super("seismic_wave", "Seismic Wave", Type.SNEAK, 30);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("If in air create a seismic wave that damages players and knocks them away.");
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onAbility(Player p) {
        if (p.isOnGround()) return false;

        Location location = p.getLocation().toBlockLocation();

        Location circle0Location = location.clone();
        List<Location> circle2Locations = MathUtils.generateCircle(location, 3, true);
        List<Location> circle3Locations = MathUtils.generateCircle(location, 4, true);
        List<Location> circle1Locations = MathUtils.generateCircle(location, 2, true);

        circle0Location.setYaw(0);
        circle0Location.setPitch(0);
        while (circle0Location.getBlock().getType() == Material.AIR) circle0Location.subtract(0, 1, 0);

        circle1Locations.forEach(location1 -> {
            // Above ground
            while (location1.getBlock().getType() == Material.AIR) location1.subtract(0, 1, 0);
            // Sub ground
            while (location1.clone().add(0, 1, 0).getBlock().getType() != Material.AIR) location1.add(0, 1, 0);
        });

        circle2Locations.forEach(location2 -> {
            // Above ground
            while (location2.getBlock().getType() == Material.AIR) location2.subtract(0, 1, 0);
            // Sub ground
            while (location2.clone().add(0, 1, 0).getBlock().getType() != Material.AIR) location2.add(0, 1, 0);
        });

        circle3Locations.forEach(location3 -> {
            // Above ground
            while (location3.getBlock().getType() == Material.AIR) location3.subtract(0, 1, 0);
            // Sub ground
            while (location3.clone().add(0, 1, 0).getBlock().getType() != Material.AIR) location3.add(0, 1, 0);
        });

        List<Entity> circle1 = new ArrayList<>();
        List<Entity> circle2 = new ArrayList<>();
        List<Entity> circle3 = new ArrayList<>();

        Entity circle0 = location.getWorld().spawnEntity(circle0Location.clone().add(0, 1, 0), EntityType.BLOCK_DISPLAY, CreatureSpawnEvent.SpawnReason.CUSTOM, block_display -> {
            ((BlockDisplay) block_display).setBlock(circle0Location.getBlock().getBlockData());
            ((BlockDisplay) block_display).setTransformation(new Transformation(new Vector3f(0, -1, 0), new Quaternionf(0, 0, 0, 1), new Vector3f(1, 1, 1), new Quaternionf(0, 0, 0, 1)));
        });

        spawnCircleLocation(location, circle1Locations, circle1);
        spawnCircleLocation(location, circle2Locations, circle2);
        spawnCircleLocation(location, circle3Locations, circle3);

        new BukkitRunnable() {
            double i = 1;

            @Override
            public void run() {
                if (i <= 20) {
                    double y0 = Math.sin(i * Math.PI * 0.1);
                    circle0.teleport(circle0.getLocation().add(0, y0 * 0.1, 0));
                }
                if (i == 20) {
                    circle0.remove();
                }

                if (i >= 5) {
                    circle1.forEach(block_display -> {
                        double y = Math.sin((i - 5) * Math.PI * 0.1);
                        block_display.teleport(block_display.getLocation().add(0, y * 0.1, 0));
                    });
                }

                if (i == 25) {
                    circle1.forEach(Entity::remove);
                    circle1.clear();
                }

                if (i >= 10) {
                    circle2.forEach(block_display -> {
                        double y = Math.sin((i - 10) * Math.PI * 0.1);
                        block_display.teleport(block_display.getLocation().add(0, y * 0.1, 0));
                    });
                }

                if (i == 30) {
                    circle2.forEach(Entity::remove);
                    circle2.clear();
                }

                if (i >= 15) {
                    circle3.forEach(block_display -> {
                        double y = Math.sin((i - 15) * Math.PI * 0.1);
                        block_display.teleport(block_display.getLocation().add(0, y * 0.1, 0));
                    });
                }

                if (i == 35) {
                    circle3.forEach(Entity::remove);
                    circle3.clear();
                    cancel();
                }

                i++;
            }
        }.runTaskTimer(KitPvp.INSTANCE, 10, 1);

        p.getWorld().playSound(location, Sound.ENTITY_GENERIC_BIG_FALL, 1, 0.1f);
        p.setVelocity(p.getVelocity().add(new Vector(0, -2, 0)));

        p.getNearbyEntities(3, 3, 3).forEach(target -> {
            if (target instanceof Player targetPlayer) {
                targetPlayer.knockback(3, p.getX(), p.getZ());
                targetPlayer.damage(10, p);
            }
        });


        return true;
    }

    private void spawnCircleLocation(Location location, List<Location> circle1Locations, List<Entity> circle1) {
        circle1Locations.forEach(location1 -> location.getWorld().spawnEntity(location1.clone().add(0, 1, 0), EntityType.BLOCK_DISPLAY, CreatureSpawnEvent.SpawnReason.CUSTOM, block_display -> {
            ((BlockDisplay) block_display).setBlock(location1.getBlock().getBlockData());
            ((BlockDisplay) block_display).setTransformation(new Transformation(new Vector3f(0, -1, 0), new Quaternionf(0, 0, 0, 1), new Vector3f(1.01f, 1.01f, 1.01f), new Quaternionf(0, 0, 0, 1)));
            circle1.add(block_display);
        }));
    }
}

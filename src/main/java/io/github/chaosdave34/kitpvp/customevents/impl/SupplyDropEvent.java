package io.github.chaosdave34.kitpvp.customevents.impl;

import io.github.chaosdave34.kitpvp.customevents.CustomEvent;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

// Todo: elytra?
public class SupplyDropEvent extends CustomEvent {
    public SupplyDropEvent() {
        super("Supply Drop", 5 * 60);
    }

    private Location location;

    @Override
    public void start() {
        Random random = new Random();

        int x = random.nextInt(30, 50) * (random.nextBoolean() ? 1 : -1);
        int z = random.nextInt(30, 50) * (random.nextBoolean() ? 1 : -1);

        location = new Location(Bukkit.getWorld("world"), x + 0.5, 200, z + 0.5);

        location.getWorld().spawnEntity(location, EntityType.FALLING_BLOCK, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
            FallingBlock fallingBlock = ((FallingBlock) entity);

            Barrel barrel = (Barrel) Material.BARREL.createBlockData().createBlockState();
            barrel.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
            fallingBlock.setBlockState(barrel);

            fallingBlock.shouldAutoExpire(false);
        });

    }

    @Override
    public void stop() {
        if (location.getBlock().getType() == Material.BARREL)
            location.getBlock().setType(Material.AIR);
    }

    @EventHandler
    public void onBarrelLoot(InventoryCloseEvent e) {
        if (location == null) return;
        if (e.getInventory().getHolder() instanceof Barrel barrel) {
            if (barrel.getLocation().equals(location)) {
                if (e.getInventory().isEmpty()) {
                    barrel.setType(Material.AIR);
                    cancelled = true;
                }
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock fallingBlock && e.getTo() == Material.BARREL) {
            if (fallingBlock.getOrigin() == null) return;

            if (fallingBlock.getOrigin().equals(location)) {
                location = e.getBlock().getLocation();

                spawnFlare(location);
            }
        }
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent e) {
        if (e.getEntity() instanceof FallingBlock fallingBlock) {
            if (fallingBlock.getOrigin() == null) return;

            if (fallingBlock.getOrigin().equals(location)) {
                e.setCancelled(true);

                Location loc = fallingBlock.getLocation();

                while (loc.getBlock().getType() != Material.AIR) {
                    loc.add(0, 1, 0);
                }

                location = fallingBlock.getLocation();

                loc.getBlock().setBlockData(fallingBlock.getBlockData());
                Barrel barrel = (Barrel) loc.getBlock().getState();
                barrel.getInventory().setContents(((Barrel) fallingBlock.getBlockState()).getInventory().getContents());

                spawnFlare(location);
            }
        }
    }

    private void spawnFlare(Location location) {
        location.getWorld().spawnEntity(location.clone().add(0, 1, 0), EntityType.FIREWORK_ROCKET, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
            Firework firework = ((Firework) entity);

            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            fireworkMeta.setPower(2);
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.BALL).build());
            firework.setFireworkMeta(fireworkMeta);
        } );
    }
}

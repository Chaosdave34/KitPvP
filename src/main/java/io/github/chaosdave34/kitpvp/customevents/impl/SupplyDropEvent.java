package io.github.chaosdave34.kitpvp.customevents.impl;

import io.github.chaosdave34.kitpvp.customevents.CustomEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

// Todo: elytra?
public class SupplyDropEvent extends CustomEvent {
    public SupplyDropEvent() {
        super("Supply Drop", 5 * 60);
    }

    Location location;

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
        Location target = location.clone();
        while (target.getBlock().getType() != Material.BARREL) {
            target.subtract(0, 1, 0);
        }

        target.getBlock().setType(Material.AIR);
    }

    @EventHandler
    public void onBarrelLoot(InventoryCloseEvent e) {
        if (location == null) return;
        if (e.getInventory().getHolder() instanceof Barrel barrel) {
            if (barrel.getLocation().getBlockX() == location.getBlockX() && barrel.getLocation().getBlockZ() == location.getBlockZ() && barrel.getWorld().equals(location.getWorld())) {
                if (e.getInventory().isEmpty()) {
                    barrel.setType(Material.AIR);
                    cancelled = true;
                }
            }
        }
    }

    @EventHandler
    public void onBlockChangeEvent(EntityDropItemEvent e) {
        if (e.getEntity() instanceof FallingBlock fallingBlock) {
            if (location.equals(fallingBlock.getOrigin())) {
                e.setCancelled(true);

                Location loc = fallingBlock.getLocation();

                while (loc.getBlock().getType() != Material.AIR) {
                    loc.add(0, 1, 0);
                }

                loc.getBlock().setBlockData(fallingBlock.getBlockData());
                Barrel barrel = (Barrel) loc.getBlock().getState();
                barrel.getInventory().setContents(((Barrel) fallingBlock.getBlockState()).getInventory().getContents());
            }
        }
    }
}

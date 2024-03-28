package io.github.chaosdave34.kitpvp.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SpawnListener implements Listener {
    private Location turbine1;
    private Location turbine2;
    private Location turbine3;
    private Location turbine4;


    // ALL Spawns
    @EventHandler
    public void onRespawnAnchorExplode(BlockExplodeEvent e) {
        if (e.getExplodedBlockState() == null) return;
        if (e.getExplodedBlockState().getType() == Material.RESPAWN_ANCHOR) {
            if (e.getBlock().getLocation().getY() > 105)
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawnAnchor(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        if (extendedPlayer.inSpawn()) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
            if (extendedPlayer.inSpawn()) {
                // Kill Command
                if (e.getCause() == EntityDamageEvent.DamageCause.KILL) return;
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDealDamage(EntityDealDamageEvent e) {
        if (e.getDamager() instanceof Player p) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
            if (extendedPlayer.inSpawn()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEnterEndPortal(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);

        e.setCancelled(true);

        if (extendedPlayer == null)
            return;

        if (extendedPlayer.inSpawn()) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("amusementpark");
                p.sendPluginMessage(KitPvp.INSTANCE, "BungeeCord", out.toByteArray());

                p.teleport(new Location(Bukkit.getWorld("world"), -12.0, 120, 16.0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 4));
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player p) {
            if (ExtendedPlayer.from(p).inSpawn()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBowShot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (ExtendedPlayer.from(p).inSpawn()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).inSpawn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).inSpawn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).inSpawn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).inSpawn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onConsumeItem(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).inSpawn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChance(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (ExtendedPlayer.from(p).inSpawn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        if (extendedPlayer != null && extendedPlayer.inSpawn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLoadCrossbow(EntityLoadCrossbowEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (ExtendedPlayer.from(p).inSpawn()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        if (extendedPlayer.inSpawn()) {
            if (e.getMaterial() == Material.TRIDENT)
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        // Normal
        if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.SPAWN) {
            // Game enter
            if (e.getTo().clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR && e.getTo().getY() <= 105) {
                extendedPlayer.setGameState(ExtendedPlayer.GameState.IN_GAME);
                p.setFallDistance(0f);
            }

            // Turbine
            Location loc = p.getLocation();
            int d = 5;
            if (loc.distance(turbine1) < d || loc.distance(turbine2) < d || loc.distance(turbine3) < d || loc.distance(turbine4) < d) {
                Vector launchVector = p.getLocation().toVector().normalize();
                launchVector.multiply(12);
                launchVector.setY(0.75);
                p.setVelocity(launchVector);
            }
        }
        // Elytra
        else if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.ELYTRA_SPAWN) {
            if (e.getTo().getY() < 197) {
                extendedPlayer.setGameState(ExtendedPlayer.GameState.ELYTRA_IN_GAME);
            }
        }
    }


    @EventHandler
    public void onOpenBarrel(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        if (ExtendedPlayer.from(p).inSpawn() && e.getInventory().getHolder() instanceof Barrel) e.setCancelled(true);
    }

    // Game Spawn
    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        if (!e.getWorld().getName().equals("world")) return;

        World world = e.getWorld();
        turbine1 = new Location(world, 2.0, 120.0, 22.0);
        turbine2 = new Location(world, -18.0, 120.0, 2.0);
        turbine3 = new Location(world, 2.0, 120.0, -18.0);
        turbine4 = new Location(world, 22.0, 120.0, 2.0);

        new BukkitRunnable() {
            @Override
            public void run() {
                world.spawnParticle(Particle.EXPLOSION_NORMAL, turbine1, 10, 2, 1, 2, 0);
                world.spawnParticle(Particle.EXPLOSION_NORMAL, turbine2, 10, 2, 1, 2, 0);
                world.spawnParticle(Particle.EXPLOSION_NORMAL, turbine3, 10, 2, 1, 2, 0);
                world.spawnParticle(Particle.EXPLOSION_NORMAL, turbine4, 10, 2, 1, 2, 0);

            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, 5);
    }

    @EventHandler
    public void onModifyArmor(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
            if (e.getClickedInventory() instanceof PlayerInventory) {
                if (e.getSlot() >= 36 && e.getSlot() < 40) {
                    e.setCancelled(true);
                }
            }
        }
    }
}

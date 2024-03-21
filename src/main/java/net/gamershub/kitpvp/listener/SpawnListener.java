package net.gamershub.kitpvp.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
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
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SpawnListener implements Listener {
    private Location turbine1;
    private Location turbine2;
    private Location turbine3;
    private Location turbine4;

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
        }.runTaskTimer(KitPvpPlugin.INSTANCE, 0, 5);
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
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        if (extendedPlayer.inSpawn()) {
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
    }

    @EventHandler
    public void onEnterEndPortal(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);

        if (extendedPlayer == null) {
            e.setCancelled(true);
            return;
        }

        if (extendedPlayer.inSpawn()) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
                e.setCancelled(true);

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("amusementpark");
                p.sendPluginMessage(KitPvpPlugin.INSTANCE, "BungeeCord", out.toByteArray());
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
        if (ExtendedPlayer.from(p).inSpawn()) {
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


}

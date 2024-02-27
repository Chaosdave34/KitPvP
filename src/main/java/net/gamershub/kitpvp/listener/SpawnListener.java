package net.gamershub.kitpvp.listener;

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class SpawnListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
            if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.SPAWN) {
                // Kill Command
                if (e.getCause() == EntityDamageEvent.DamageCause.KILL) return;
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEnterGameEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.SPAWN) {
            if (e.getTo().clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR && e.getTo().getY() <= 90) {
                extendedPlayer.setGameState(ExtendedPlayer.GameState.IN_GAME);
                p.setFallDistance(0f);
            }
        }

    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player p) {
            if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBowShot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onConsumeItem(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChance(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLoadCrossbow(EntityLoadCrossbowEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.SPAWN) {
            // Launchpad
            if (p.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.SLIME_BLOCK) {
                Vector launchVector = p.getLocation().toVector().normalize();
                launchVector.multiply(5);
                launchVector.setY(0.5);
                p.setVelocity(launchVector);
            }
        }
    }
}

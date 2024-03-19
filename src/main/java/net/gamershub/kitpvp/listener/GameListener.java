package net.gamershub.kitpvp.listener;

import io.papermc.paper.event.entity.EntityMoveEvent;
import lombok.Getter;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameListener implements Listener {
    @Getter
    private final Map<Location, Long> blocksToRemove = new ConcurrentHashMap<>();
    private BukkitTask blockRemoverTask;

    public void startBlockRemover() {
        if (blockRemoverTask == null || !Bukkit.getScheduler().isCurrentlyRunning(blockRemoverTask.getTaskId())) {
            blockRemoverTask = new BlockRemover().runTaskTimer(KitPvpPlugin.INSTANCE, 0, 20);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            Block block = e.getBlock();

            if (e.getBlock().getType() == Material.FIRE) return;

            if (e.getBlock().getLocation().getY() > 90) {
                e.setCancelled(true);
                return;
            }

            block.setMetadata("placed_by_player", new FixedMetadataValue(KitPvpPlugin.INSTANCE, true));

            blocksToRemove.put(block.getLocation(), System.currentTimeMillis());

            startBlockRemover();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            if (!e.getBlock().hasMetadata("placed_by_player")) {
                if (e.getBlock().getType() == Material.FIRE) return;

                blocksToRemove.remove(e.getBlock().getLocation());
                e.setCancelled(true);
            }
            if (e.getBlock().getType() == Material.COBWEB) e.setDropItems(false);
        }
    }

    @EventHandler
    public void onGravityBlockChangeState(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock fallingBlock) {
            Block block = e.getBlock();

            if (fallingBlock.hasMetadata("placed_by_player")) {
                block.setMetadata("placed_by_player", new FixedMetadataValue(KitPvpPlugin.INSTANCE, true));
                blocksToRemove.put(block.getLocation(), System.currentTimeMillis());
            } else if (block.hasMetadata("placed_by_player")) {
                fallingBlock.setMetadata("placed_by_player", new FixedMetadataValue(KitPvpPlugin.INSTANCE, true));
                blocksToRemove.remove(block.getLocation());
            }
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            Block block = e.getBlock();

            if (e.getBlock().getLocation().getY() > 90) {
                e.setCancelled(true);
                return;
            }

            block.setMetadata("placed_by_player", new FixedMetadataValue(KitPvpPlugin.INSTANCE, true));

            blocksToRemove.put(block.getLocation(), System.currentTimeMillis());

            startBlockRemover();
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            if (!e.getBlock().hasMetadata("placed_by_player")) {
                blocksToRemove.remove(e.getBlock().getLocation());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChance(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageByEntityEvent) {
            if (damageByEntityEvent.getDamager() instanceof Player damager) {
                ExtendedPlayer extendedDamager = KitPvpPlugin.INSTANCE.getExtendedPlayer(damager);
                if (extendedDamager.getGameState() == ExtendedPlayer.GameState.IN_GAME) {
                   extendedDamager.killedPlayer(e.getEntity());
                }
            }
        }
    }

    @EventHandler
    public void onDurabilityReduction(PlayerItemDamageEvent e) {
        Player p = e.getPlayer();
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onDismountMorph(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player p) {
            ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
            if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.IN_GAME) {
                if (p.getUniqueId().equals(extendedPlayer.getUuid())) {
                    if (extendedPlayer.getMorph() == null) return;
                    extendedPlayer.unmorph();
                }
            }
        }
    }

    @EventHandler
    public void onMorphMove(EntityMoveEvent e) {
        Entity morph = e.getEntity();
        if (morph.hasMetadata("morph")) {
            Player p = (Player) morph.getPassengers().get(0);

            if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
                morph.setVelocity(p.getEyeLocation().getDirection().multiply(0.5));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
            if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.IN_GAME) {
                extendedPlayer.enterCombat();
            }
        }
    }

    @EventHandler
    public void onDealDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager) {
            ExtendedPlayer extendedDamager = KitPvpPlugin.INSTANCE.getExtendedPlayer(damager);
            if (extendedDamager.getGameState() == ExtendedPlayer.GameState.IN_GAME) {
                extendedDamager.enterCombat();
            }
        }
    }

    private class BlockRemover extends BukkitRunnable {
        @Override
        public void run() {
            if (blocksToRemove.isEmpty()) {
                this.cancel();
            }

            long currentTime = System.currentTimeMillis();
            Iterator<Map.Entry<Location, Long>> iterator = blocksToRemove.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<Location, Long> entry = iterator.next();
                if (currentTime - entry.getValue() >= 30 * 1000) {
                    Block block = entry.getKey().getBlock();
                    if (block instanceof Waterlogged)
                        ((Waterlogged) block).setWaterlogged(false);
                    else
                        block.setType(Material.AIR);
                    iterator.remove();
                }
            }
        }
    }
}

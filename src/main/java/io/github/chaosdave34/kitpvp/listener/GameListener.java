package io.github.chaosdave34.kitpvp.listener;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.damagetype.DamageTypes;
import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameListener implements Listener {
    @Getter
    private final Map<Location, Long> blocksToRemove = new ConcurrentHashMap<>();
    private BukkitTask blockRemoverTask;

    // All
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        if (extendedPlayer == null || extendedPlayer.inSpawn()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChance(FoodLevelChangeEvent e) {
        Player p = (Player) e.getEntity();
        if (ExtendedPlayer.from(p).inGame()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDurabilityReduction(PlayerItemDamageEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).inGame()) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onDismountMorph(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player p) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
            if (extendedPlayer.inGame()) {
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
            if (morph.getPassengers().isEmpty()) morph.remove();
            Player p = (Player) morph.getPassengers().get(0);

            if (ExtendedPlayer.from(p).inGame()) {
                morph.setVelocity(p.getEyeLocation().getDirection().multiply(0.5));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
            if (extendedPlayer.inGame()) {
                extendedPlayer.enterCombat();
            }
        }
    }

    @EventHandler
    public void onDealDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager) {
            ExtendedPlayer extendedDamager = ExtendedPlayer.from(damager);
            if (extendedDamager.inGame()) {
                extendedDamager.enterCombat();
            }
        }
    }

    @EventHandler
    public void onRespawnAnchor(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        if (extendedPlayer.inGame()) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) {
                    if (e.getClickedBlock().getLocation().getY() > 105)
                        e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onRocketLaunch(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (ExtendedPlayer.from(p).inGame()) {
            if (e.getMaterial() == Material.FIREWORK_ROCKET && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLoadCrossbow(EntityLoadCrossbowEvent e) {
        if (e.getEntity() instanceof Player p) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
            if (extendedPlayer.inGame()) {
                if (e.getCrossbow().containsEnchantment(Enchantment.ARROW_INFINITE))
                    e.setConsumeItem(false);

                ItemStack crossbow = e.getCrossbow();
                if (CustomItemHandler.ROCKET_LAUNCHER.getId().equals(CustomItemHandler.getCustomItemId(crossbow))
                        || extendedPlayer.getGameState() == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {

                    Bukkit.getScheduler().runTaskLater(KitPvp.INSTANCE, () -> crossbow.editMeta(CrossbowMeta.class, crossbowMeta -> {

                        List<ItemStack> projectiles = new ArrayList<>();

                        for (ItemStack projectile : crossbowMeta.getChargedProjectiles()) {
                            if (projectile.getType() == Material.FIREWORK_ROCKET) {
                                projectile.editMeta(FireworkMeta.class, fireworkMeta -> {
                                    Random random = new Random();
                                    Color randomColor = Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                                    FireworkEffect.Type randomType = FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)];

                                    fireworkMeta.addEffect(FireworkEffect.builder().withColor(randomColor).with(randomType).build());
                                });
                                projectiles.add(projectile);
                            }
                        }

                        if (!projectiles.isEmpty())
                            crossbowMeta.setChargedProjectiles(projectiles);

                        crossbow.setItemMeta(crossbowMeta);
                    }), 1);


                }
            }
        }
    }

    @EventHandler
    public void onOpenAnvil(InventoryOpenEvent e) {
        if (e.getInventory() instanceof AnvilInventory) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        e.setDroppedExp(0);
        e.getDrops().clear();
    }

    // KitPvP
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
                    if (block.getBlockData() instanceof Waterlogged waterlogged) {
                        waterlogged.setWaterlogged(false);
                        block.setBlockData(waterlogged);
                    } else
                        block.setType(Material.AIR);
                    iterator.remove();
                }
            }
        }
    }

    public void startBlockRemover() {
        if (blockRemoverTask == null || !Bukkit.getScheduler().isCurrentlyRunning(blockRemoverTask.getTaskId())) {
            blockRemoverTask = new BlockRemover().runTaskTimer(KitPvp.INSTANCE, 0, 20);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            Block block = e.getBlock();

            if (e.getBlock().getType() == Material.FIRE) return;

            if (e.getBlock().getLocation().getY() > 105) {
                e.setCancelled(true);
                return;
            }

            // Todo: replace with old block instead of cancelling
            if (e.getBlockReplacedState().getType() != Material.AIR) {
                if (!e.getBlockReplacedState().hasMetadata("placed_by_player")) {
                    e.setCancelled(true);
                    return;
                }
            }

            block.setMetadata("placed_by_player", new FixedMetadataValue(KitPvp.INSTANCE, true));

            blocksToRemove.put(block.getLocation(), System.currentTimeMillis());

            startBlockRemover();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            if (!e.getBlock().hasMetadata("placed_by_player")) {
                if (e.getBlock().getType() == Material.FIRE) return;

                e.setCancelled(true);
            } else {
                blocksToRemove.remove(e.getBlock().getLocation());
            }

            if (e.getBlock().getType() == Material.COBWEB) e.setDropItems(false);
        }
    }

    @EventHandler
    public void onGravityBlockChangeState(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock fallingBlock) {
            Block block = e.getBlock();

            if (fallingBlock.hasMetadata("placed_by_player")) {
                block.setMetadata("placed_by_player", new FixedMetadataValue(KitPvp.INSTANCE, true));
                blocksToRemove.put(block.getLocation(), System.currentTimeMillis());
            } else if (block.hasMetadata("placed_by_player")) {
                fallingBlock.setMetadata("placed_by_player", new FixedMetadataValue(KitPvp.INSTANCE, true));
                blocksToRemove.remove(block.getLocation());
            }
        }
    }

    @EventHandler
    public void onBlockBreakCobWeb(BlockBreakBlockEvent e) {
        if (e.getSource().getType() == Material.WATER) {
            if (e.getBlock().getType() == Material.COBWEB) e.getDrops().clear();
        }
    }


    @EventHandler
    public void onBlockBreakGrass(BlockFromToEvent e) {
        if (e.getBlock().getType() == Material.WATER) {
            Block target = e.getToBlock();
            if (target.getType() != Material.AIR) {

                if (!target.hasMetadata("placed_by_player")) {
                    if (target.getType() == Material.FIRE) return;

                    e.setCancelled(true);
                } else {
                    blocksToRemove.remove(target.getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            Block block = e.getBlock();

            if (e.getBlock().getLocation().getY() > 105) {
                e.setCancelled(true);
                return;
            }

            block.setMetadata("placed_by_player", new FixedMetadataValue(KitPvp.INSTANCE, true));

            blocksToRemove.put(block.getLocation(), System.currentTimeMillis());

            startBlockRemover();
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            if (!e.getBlock().hasMetadata("placed_by_player")) {
                e.setCancelled(true);
            } else {
                blocksToRemove.remove(e.getBlock().getLocation());
            }
        }
    }

    // Elytra
    @EventHandler
    public void onBlockPlaceElytra(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreakElytra(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmptyElytra(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFillElytra(PlayerBucketFillEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMoveElytra(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {

            Material blockBelow = e.getTo().clone().subtract(0, 1, 0).getBlock().getType();
            List<Material> filterForBlockBelow = List.of(Material.GRASS_BLOCK, Material.SAND, Material.SPRUCE_LEAVES, Material.OAK_LEAVES);


            if (e.getTo().getBlock().getType() == Material.WATER)
                p.damage(Integer.MAX_VALUE, DamageSource.builder(DamageType.DROWN).build());

            else if (filterForBlockBelow.contains(blockBelow))
                p.damage(Integer.MAX_VALUE, DamageSource.builder(DamageTypes.LAND).build());

            else if (e.getTo().getY() > 199)
                p.damage(Integer.MAX_VALUE, DamageSource.builder(DamageTypes.ESCAPE).build());
        }
    }

    @EventHandler
    public void onRocketLaunch(PlayerElytraBoostEvent e) {
        Player p = e.getPlayer();
        if (ExtendedPlayer.from(p).getGameState() == ExtendedPlayer.GameState.ELYTRA_IN_GAME) {
            e.setShouldConsume(false);
        }
    }
}

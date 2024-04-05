package io.github.chaosdave34.kitpvp.items.impl.artilleryman;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Jetpack extends CustomItem {
    private final Map<UUID, Integer> refillTasks = new HashMap<>();

    public Jetpack() {
        super(Material.LEATHER_CHESTPLATE, "jetpack");
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Jetpack");
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
        setLeatherArmorColor(itemStack, Color.NAVY);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);

        if (extendedPlayer.inGame()) {
            ItemStack jetpack = p.getInventory().getChestplate();
            if (jetpack == null) return;

            if (CustomItemHandler.JETPACK.getId().equals(CustomItemHandler.getCustomItemId(jetpack))) {

                if (e.isSneaking()) {
                    if (refillTasks.containsKey(p.getUniqueId()))
                        Bukkit.getScheduler().cancelTask(refillTasks.remove(p.getUniqueId()));

                    Damageable jetpackMeta = (Damageable) jetpack.getItemMeta();

                    if (jetpackMeta.getDamage() < 80 && p.getVelocity().length() < 0.1) {
                        p.setVelocity(p.getVelocity().setY(0.5));
                    }

                } else {
                    if (!refillTasks.containsKey(p.getUniqueId())) {
                        BukkitTask task = new RefillTask(p).runTaskTimer(KitPvp.INSTANCE, 20, 20);
                        refillTasks.put(p.getUniqueId(), task.getTaskId());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);

        if (extendedPlayer.inGame()) {
            ItemStack jetpack = p.getInventory().getChestplate();
            if (jetpack == null) return;

            if (CustomItemHandler.JETPACK.getId().equals(CustomItemHandler.getCustomItemId(jetpack))) {

                if (p.isSneaking()) {
                    Damageable jetpackMeta = (Damageable) jetpack.getItemMeta();

                    Vector verticalMovement = e.getTo().clone().subtract(e.getFrom()).toVector().setY(p.getVelocity().getY());

                    if (jetpackMeta.getDamage() < 80) {
                        if (p.getVelocity().getY() <= 0.3) {
                            if (p.getVelocity().getY() < 0)
                                p.setVelocity(p.getVelocity().add(new Vector(0, 0.1, 0)));
                            else
                                p.setVelocity(p.getVelocity().add(new Vector(0, 0.3, 0)));
                            jetpackMeta.setDamage(jetpackMeta.getDamage() + 1);
                            jetpack.setItemMeta(jetpackMeta);
                        }

                        if (verticalMovement.length() > 0.01) {
                            p.setVelocity(p.getEyeLocation().getDirection().multiply(0.5).setY(p.getVelocity().getY()));
                        }

                        p.sendActionBar(Component.text("Jetpack Fuel: " + (80 - jetpackMeta.getDamage()) + "/80"));
                    }
                }
            }
        }
    }


    @EventHandler
    public void onSpawn(PlayerSpawnEvent e) {
        Player p = e.getPlayer();
        if (refillTasks.containsKey(p.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(refillTasks.get(p.getUniqueId()));
            refillTasks.remove(p.getUniqueId());
        }
    }

    class RefillTask extends BukkitRunnable {
        private final Player player;

        public RefillTask(Player player) {
            this.player = player;
        }

        @Override
        public void run() {
            if (player.getInventory().getChestplate() != null) {
                ItemStack jetpack = player.getInventory().getChestplate();
                Damageable jetpackMeta = (Damageable) jetpack.getItemMeta();
                if (jetpackMeta.getDamage() != 0) {
                    jetpackMeta.setDamage(jetpackMeta.getDamage() - 1);
                    jetpack.setItemMeta(jetpackMeta);
                    player.sendActionBar(Component.text("Jetpack Fuel: " + (80 - jetpackMeta.getDamage()) + "/80"));
                } else {
                    refillTasks.remove(player.getUniqueId());
                    this.cancel();
                }
            }
        }
    }

}

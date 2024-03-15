package net.gamershub.kitpvp.items.impl.artilleryman;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.events.PlayerSpawnEvent;
import net.gamershub.kitpvp.items.CustomItem;
import net.gamershub.kitpvp.kits.KitHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
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
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            if (extendedPlayer.getSelectedKit() == KitHandler.ARTILLERYMAN) {
                if (p.isSneaking()) {
                    if (p.getInventory().getChestplate() != null) {
                        if (refillTasks.containsKey(p.getUniqueId())) {
                            Bukkit.getScheduler().cancelTask(refillTasks.remove(p.getUniqueId()));
                        }

                        ItemStack jetpack = p.getInventory().getChestplate();
                        Damageable jetpackMeta = (Damageable) jetpack.getItemMeta();

                        Vector verticalMovement = e.getTo().clone().subtract(e.getFrom()).toVector().setY(p.getVelocity().getY());

                        if (jetpackMeta.getDamage() < 80) {
                            if (p.getVelocity().getY() <= 0.3) {
                                p.setVelocity(p.getVelocity().add(new Vector(0, 0.1, 0)));
                                jetpackMeta.setDamage(jetpackMeta.getDamage() + 1);
                                jetpack.setItemMeta(jetpackMeta);
                            }

                            if (verticalMovement.length() > 0.01) {
                                p.setVelocity(p.getEyeLocation().getDirection().multiply(0.5).setY(p.getVelocity().getY()));
                            }

                            p.sendActionBar(Component.text("Jetpack Fuel: " + (80 - jetpackMeta.getDamage()) + "/80"));
                        }
                    }
                } else {
                    if (!refillTasks.containsKey(p.getUniqueId())) {
                        BukkitTask task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (p.getInventory().getChestplate() != null) {
                                    ItemStack jetpack = p.getInventory().getChestplate();
                                    Damageable jetpackMeta = (Damageable) jetpack.getItemMeta();
                                    if (jetpackMeta.getDamage() != 0) {
                                        jetpackMeta.setDamage(jetpackMeta.getDamage() - 1);
                                        jetpack.setItemMeta(jetpackMeta);
                                        p.sendActionBar(Component.text("Jetpack Fuel: " + (80 - jetpackMeta.getDamage()) + "/80"));
                                    } else {
                                        refillTasks.remove(p.getUniqueId());
                                        this.cancel();
                                    }
                                }
                            }
                        }.runTaskTimer(KitPvpPlugin.INSTANCE, 0, 10);
                        refillTasks.put(p.getUniqueId(), task.getTaskId());
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

}

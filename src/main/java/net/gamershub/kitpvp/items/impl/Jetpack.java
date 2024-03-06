package net.gamershub.kitpvp.items.impl;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.items.CustomItem;
import net.gamershub.kitpvp.kits.KitHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Jetpack extends CustomItem {
    public Jetpack() {
        super(Material.LEATHER_CHESTPLATE, "jetpack", false);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Jetpack");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            if (extendedPlayer.getSelectedKit() == KitHandler.CROSSBOW) {
                if (p.isSneaking()) {
                    if(p.getInventory().getChestplate() != null){
                        double xVelocity = p.getVelocity().getX();
                        double zVelocity = p.getVelocity().getZ();
                        ItemStack jetpack = p.getInventory().getChestplate();
                        Damageable jetpackMeta = (Damageable) jetpack.getItemMeta();
                        if (jetpackMeta.getDamage() <= 200 && p.getVelocity().getY() <= 0.5) {

                            p.setVelocity(p.getVelocity().add(new Vector(xVelocity * 50,0.25,zVelocity * 50)));
                            jetpackMeta.setDamage(jetpackMeta.getDamage() + 1);
                            jetpack.setItemMeta(jetpackMeta);
                        }
                    }
                }
            }
        }
    }

}

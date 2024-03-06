package net.gamershub.kitpvp.gui.impl;

import net.gamershub.kitpvp.gui.Gui;
import net.gamershub.kitpvp.gui.GuiHandler;
import net.gamershub.kitpvp.gui.InventoryClickHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class CosmeticsGui extends Gui {
    public CosmeticsGui() {
        super(4, Component.text("Cosmetics"), true);
    }

    @Override
    public @NotNull Inventory build(Player p, Inventory inventory) {
        inventory.setItem(11, createItemStack(Material.ARROW, "Projectile Trails", true));
        inventory.setItem(15, createItemStack(Material.IRON_SWORD, "Kill Effects", true));
        inventory.setItem(31, createItemStack(Material.BARRIER, "Close", true));
        return inventory;
    }

    @InventoryClickHandler(slot = 11)
    public void onProjectileTrails(InventoryClickEvent e) {
        GuiHandler.PROJECTILE_TRAILS.show((Player) e.getWhoClicked());
    }

    @InventoryClickHandler(slot = 15)
    public void onKillEffects(InventoryClickEvent e) {
        GuiHandler.KILL_EFFECTS.show((Player) e.getWhoClicked());
    }

    @InventoryClickHandler(slot = 31)
    public void onCloseButton(InventoryClickEvent e) {
        e.getInventory().close();
    }
}

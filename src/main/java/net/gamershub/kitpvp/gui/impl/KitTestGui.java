package net.gamershub.kitpvp.gui.impl;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.gui.Gui;
import net.gamershub.kitpvp.gui.InventoryClickHandler;
import net.gamershub.kitpvp.kits.KitHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class KitTestGui extends Gui {
    public KitTestGui() {
        super(1, Component.text("Test Kits", NamedTextColor.RED, TextDecoration.BOLD));

        setItem(0, Material.WOODEN_SWORD, Component.text("Test Kit").decoration(TextDecoration.ITALIC, false), true);
        setItem(8, Material.BARRIER, Component.text("RESET").decoration(TextDecoration.ITALIC, false), true);
    }

    @InventoryClickHandler(slot = 0)
    public void onTestKitButton(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        KitHandler.TEST.apply(p);
        p.sendMessage(Component.text("Selected Test Kit!"));
        e.setCancelled(true);
        Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, () -> p.closeInventory(), 1);
    }

    @InventoryClickHandler(slot = 8)
    public void onResetButton(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        KitPvpPlugin.INSTANCE.getKitHandler().clearKit(p);
        p.sendMessage(Component.text("Reset Kit!"));
        e.setCancelled(true);
        Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, () -> p.closeInventory(), 1);
    }
}

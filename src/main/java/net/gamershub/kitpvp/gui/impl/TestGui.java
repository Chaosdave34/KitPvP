package net.gamershub.kitpvp.gui.impl;

import net.gamershub.kitpvp.gui.Gui;
import net.gamershub.kitpvp.gui.InventoryClickHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TestGui extends Gui {
    public TestGui() {
        super(1, Component.text("Test Inventory", NamedTextColor.RED));

        setItem(1, new ItemStack(Material.DIAMOND_SWORD));
        setItem(2, Material.IRON_SWORD, Component.text(1).decoration(TextDecoration.ITALIC, false), true);
    }

    @InventoryClickHandler(slot = 1)
    public void onTestButton(InventoryClickEvent e) {
        e.getWhoClicked().sendMessage(Component.text("1"));
        e.setCancelled(true);
    }
}

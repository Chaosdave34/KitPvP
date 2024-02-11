package net.gamershub.kitpvp.gui;

import net.gamershub.kitpvp.gui.impl.TestGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.*;

public class GuiHandler implements Listener {
    private final Map<UUID, Gui> openGuis = new HashMap<>();

    public static Gui TEST;

    public GuiHandler() {
        TEST = new TestGui();
    }

    public void openGui(Player p, Gui gui) {
        p.openInventory(gui.build());
        openGuis.put(p.getUniqueId(), gui);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (openGuis.containsKey(e.getPlayer().getUniqueId())) {
            openGuis.get(e.getPlayer().getUniqueId()).onInventoryClose(e);
        }
        openGuis.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (openGuis.containsKey(e.getWhoClicked().getUniqueId())) {
            openGuis.get(e.getWhoClicked().getUniqueId()).onInventoryClick(e);
        }
    }
}

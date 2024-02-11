package net.gamershub.kitpvp.gui;

import lombok.Getter;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Gui {
    protected final int rows;
    protected final Component title;
    protected final Map<Integer, ItemStack> content = new HashMap<>();

    protected final Map<Integer, Method> inventoryClickHandlers = new HashMap<>();

    public Gui(int rows, Component title) {
        this.rows = rows;
        this.title = title;

        Class<?> clazz = this.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(InventoryClickHandler.class)) {
                inventoryClickHandlers.put(method.getAnnotation(InventoryClickHandler.class).slot(), method);
            }
        }
    }

    protected void setItem(int slot, ItemStack itemStack) {
        if (slot > 0 && slot < rows * 9) {
            content.put(slot, itemStack);
        }
    }

    protected void setItem(int slot, Material material, Component name, boolean hideAttributes) {
        if (slot > 0 && slot < rows * 9) {
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(name);
            if (hideAttributes) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }
            itemStack.setItemMeta(itemMeta);
            content.put(slot, itemStack);
        }
    }

    public Inventory build() {
        Inventory inv = Bukkit.createInventory(null, rows * 9, title);
        for (int slot : content.keySet()) {
            inv.setItem(slot, content.get(slot));
        }
        return inv;
    }

    public void show(Player p) {
        KitPvpPlugin.INSTANCE.getGuiHandler().openGui(p, this);
    }

    public void onInventoryClose(InventoryCloseEvent e) {
    }

    public void onInventoryClick(InventoryClickEvent e) {
        if (inventoryClickHandlers.containsKey(e.getSlot())) {
            try {
                inventoryClickHandlers.get(e.getSlot()).invoke(this, e);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                KitPvpPlugin.INSTANCE.getLogger().warning("Error while executing button handler method.");
            }
        }
    }
}

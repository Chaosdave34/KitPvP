package net.gamershub.kitpvp.items;

import net.gamershub.kitpvp.KitPvpPlugin;

import java.util.HashMap;
import java.util.Map;

public class CustomItems {
    public static Map<String, CustomItem> ID_MAP = new HashMap<>();

    public static CustomItem FIREBALL = createItem(new FireballWand());
    public static CustomItem LIGHTING_WAND = createItem(new LightingWand());
    private static CustomItem createItem(CustomItem item) {
        KitPvpPlugin.INSTANCE.getServer().getPluginManager().registerEvents(item, KitPvpPlugin.INSTANCE);
        return ID_MAP.put(item.id, item);
    }
}

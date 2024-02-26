package net.gamershub.kitpvp.items;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.items.impl.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class CustomItemHandler {
    public Map<String, CustomItem> ID_MAP = new HashMap<>();

    public static CustomItem FIREBALL;
    public static CustomItem LIGHTNING_WAND;
    public static CustomItem HAUNT_WAND;
    public static CustomItem MAGIC_WAND;
    public static CustomItem VAMPIRE_WAND;

    public CustomItemHandler() {
        FIREBALL = createItem(new FireballWand());
        LIGHTNING_WAND = createItem(new LightningWand());
        HAUNT_WAND = createItem(new HauntWand());
        MAGIC_WAND = createItem(new MagicWand());
        VAMPIRE_WAND = createItem(new VampireWand());
    }

    private CustomItem createItem(CustomItem item) {
        Utils.registerEvents(item);
        ID_MAP.put(item.id, item);
        return item;
    }

    public static String getCustomItemId(ItemStack itemStack) {
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(KitPvpPlugin.INSTANCE, "id");
        if (container.has(key)) {
            return container.get(key, PersistentDataType.STRING);
        }
        return null;
    }

    public void updateCustomItems(Player p) {
        ItemStack[] content = p.getInventory().getContents();

        for (int i = 0; i < content.length; i++) {
            ItemStack itemStack = content[i];

            if (itemStack == null) continue;

            String id = getCustomItemId(itemStack);
            if (id == null) continue;

            CustomItem customItem = ID_MAP.get(id);

            content[i] = customItem.build(itemStack.getAmount());
        }

        p.getInventory().setContents(content);
    }
}

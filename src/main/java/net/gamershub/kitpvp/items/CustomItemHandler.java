package net.gamershub.kitpvp.items;

import net.gamershub.kitpvp.KitPvpPlugin;
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

    public CustomItemHandler() {
        FIREBALL = createItem(new FireballWand());
        LIGHTNING_WAND = createItem(new LightningWand());
    }

    private CustomItem createItem(CustomItem item) {
        KitPvpPlugin.INSTANCE.getServer().getPluginManager().registerEvents(item, KitPvpPlugin.INSTANCE);
        return ID_MAP.put(item.id, item);
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

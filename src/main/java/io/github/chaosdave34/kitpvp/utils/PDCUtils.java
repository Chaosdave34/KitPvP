package io.github.chaosdave34.kitpvp.utils;

import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.persistentdatatypes.UUIDPersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PDCUtils {
    private static final NamespacedKey OWNER_KEY = new NamespacedKey(KitPvp.INSTANCE, "owner");
    private static final NamespacedKey ID_KEY = new NamespacedKey(KitPvp.INSTANCE, "id");

    public static void setOwner(PersistentDataHolder holder, UUID uuid) {
        PersistentDataContainer container = holder.getPersistentDataContainer();

        container.set(OWNER_KEY, new UUIDPersistentDataType(), uuid);
    }

    @Nullable
    public static UUID getOwner(PersistentDataHolder holder) {
        PersistentDataContainer container = holder.getPersistentDataContainer();

        if (container.has(OWNER_KEY)) {
            return container.get(OWNER_KEY, new UUIDPersistentDataType());
        }

        return null;
    }

    public static void setId(PersistentDataHolder holder, String id) {
        PersistentDataContainer container = holder.getPersistentDataContainer();

        container.set(ID_KEY, PersistentDataType.STRING, id);
    }

    @Nullable
    public static String getId(PersistentDataHolder holder) {
        PersistentDataContainer container = holder.getPersistentDataContainer();

        if (container.has(ID_KEY)) {
            return container.get(ID_KEY, PersistentDataType.STRING);
        }

        return null;
    }
}

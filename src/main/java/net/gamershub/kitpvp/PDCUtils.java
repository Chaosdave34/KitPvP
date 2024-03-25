package net.gamershub.kitpvp;

import net.gamershub.kitpvp.persistentdatatypes.UUIDPersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PDCUtils {
    private static final NamespacedKey OWNER_KEY = new NamespacedKey(KitPvpPlugin.INSTANCE, "owner");

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
}

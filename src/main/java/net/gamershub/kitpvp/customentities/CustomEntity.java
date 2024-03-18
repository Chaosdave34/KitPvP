package net.gamershub.kitpvp.customentities;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import lombok.Getter;
import net.gamershub.kitpvp.KitPvpPlugin;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@Getter
public abstract class CustomEntity implements Listener {
    protected String id;

    public CustomEntity(String id) {
        this.id = id;
    }

    public abstract void spawn(Player p, Location location);

    public abstract void onAddToWorld(EntityAddToWorldEvent e);

    protected boolean checkCustomEntity(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();

        NamespacedKey customEntityKey = new NamespacedKey(KitPvpPlugin.INSTANCE, "custom_entity");
        if (container.has(customEntityKey)) {
            String id = container.get(customEntityKey, PersistentDataType.STRING);
            return this.id.equals(id);
        }

        return false;
    }
}

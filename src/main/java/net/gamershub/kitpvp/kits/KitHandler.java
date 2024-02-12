package net.gamershub.kitpvp.kits;

import lombok.Getter;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.kits.impl.TestKit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KitHandler {
    private final Map<String, Kit> kits = new HashMap<>();

    public static Kit TEST;

    public KitHandler() {
        TEST = createKit(new TestKit());
    }

    private Kit createKit(Kit kit) {
        kits.put(kit.getId(), kit);
        Utils.registerEvents(kit);
        return kit;
    }

    public void clearKit(Player p) {
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        extendedPlayer.setSelectedKitId(null);

        AttributeInstance maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) maxHealth.setBaseValue(20);

        AttributeInstance movementSpeed = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (movementSpeed != null) movementSpeed.setBaseValue(0.1);

        p.getInventory().clear();
    }

    private void resetAttribute(Player p, Attribute attribute) {
        AttributeInstance instance = p.getAttribute(attribute);
        if (instance != null) instance.setBaseValue(instance.getDefaultValue());
    }
}

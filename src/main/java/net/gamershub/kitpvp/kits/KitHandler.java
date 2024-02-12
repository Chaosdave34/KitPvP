package net.gamershub.kitpvp.kits;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.kits.impl.TestKit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class KitHandler {
    public static Kit TEST;

    public KitHandler() {
        TEST = createKit(new TestKit());
    }

    private Kit createKit(Kit kit) {
        Utils.registerEvents(kit);
        return kit;
    }

    public void clearKit(Player p) {
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        extendedPlayer.setSelectedKit(null);

        resetAttribute(p, Attribute.GENERIC_MAX_HEALTH);
        resetAttribute(p, Attribute.GENERIC_MOVEMENT_SPEED);

        p.getInventory().clear();
    }

    private void resetAttribute(Player p, Attribute attribute) {
        AttributeInstance instance = p.getAttribute(attribute);
        if (instance != null) instance.setBaseValue(instance.getDefaultValue());
    }
}

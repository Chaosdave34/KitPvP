package net.gamershub.kitpvp.kits;

import lombok.Getter;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@Getter
public class Kit implements Listener {
    private final String id;
    private final String name;

    public Kit(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected double getMaxHealth() {
        return 20;
    }

    protected double getMovementSpeed() {
        return 0.1;
    }

    public ItemStack[] getArmorContents() {
        return new ItemStack[4];
    }

    // Todo: Fix movement speed base modification resetting on player re-login
    public void apply(Player p) {
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        extendedPlayer.setSelectedKitId(id);

        AttributeInstance maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) maxHealth.setBaseValue(getMaxHealth());

        AttributeInstance movementSpeed = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (movementSpeed != null) movementSpeed.setBaseValue(getMovementSpeed());

        PlayerInventory inv = p.getInventory();
        inv.setArmorContents(getArmorContents());
    }
}

package net.gamershub.kitpvp.cosmetics;

import org.bukkit.Location;
import org.bukkit.Material;

public abstract class KillEffect extends Cosmetic{
    public KillEffect(String id, String name, int levelRequirement, Material icon) {
        super(id, name, levelRequirement, icon);
    }

    public abstract void playEffect(Location location);
}

package io.github.chaosdave34.kitpvp.cosmetics;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;

@Getter
public abstract class ProjectileTrail extends Cosmetic {

    public ProjectileTrail(String id, String name, int levelRequirement, Material icon) {
        super(id, name, levelRequirement, icon);
    }

    public abstract void playEffect(Location location);
}

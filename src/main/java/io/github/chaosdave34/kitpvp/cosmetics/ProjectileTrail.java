package io.github.chaosdave34.kitpvp.cosmetics;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Particle;

@Getter
public class ProjectileTrail extends Cosmetic {
    protected Particle particle;

    public ProjectileTrail(String id, String name, Particle particle, int levelRequirement, Material icon) {
        super(id, name, levelRequirement, icon);
        this.particle = particle;
    }
}

package io.github.chaosdave34.kitpvp.cosmetics.impl;

import io.github.chaosdave34.kitpvp.cosmetics.ProjectileTrail;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

public class SimpleProjectileTrail extends ProjectileTrail {
    protected Particle particle;

    public SimpleProjectileTrail(String id, String name, Particle particle, int levelRequirement, Material icon) {
        super(id, name, levelRequirement, icon);
        this.particle = particle;
    }

    @Override
    public void playEffect(Location location) {
        location.getWorld().spawnParticle(particle, location, 1, 0, 0, 0, 0);
    }
}

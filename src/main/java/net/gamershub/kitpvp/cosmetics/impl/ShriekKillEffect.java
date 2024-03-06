package net.gamershub.kitpvp.cosmetics.impl;

import net.gamershub.kitpvp.cosmetics.KillEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

public class ShriekKillEffect extends KillEffect {
    public ShriekKillEffect() {
        super("shriek", "Shriek", 1, Material.SCULK_SHRIEKER);
    }

    @Override
    public void playEffect(Location location) {
        location.getWorld().spawnParticle(Particle.SHRIEK, location, 10, 0, 0, 0, 0, 1);
    }
}

package net.gamershub.kitpvp.cosmetics.impl;

import net.gamershub.kitpvp.cosmetics.KillEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class SmokeKillEffect extends KillEffect {
    public SmokeKillEffect() {
        super("smoke", "Smoke", 1, Material.CAMPFIRE);
    }

    @Override
    public void playEffect(Location location) {
        location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location.add(new Vector(0, 1, 0)), 10);
    }
}

package io.github.chaosdave34.kitpvp.cosmetics.impl;

import io.github.chaosdave34.kitpvp.cosmetics.ProjectileTrail;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

public class NoteProjectileTrail extends ProjectileTrail {
    public NoteProjectileTrail() {
        super("note", "Note", 11, Material.NOTE_BLOCK);
    }

    @Override
    public void playEffect(Location location) {
        location.getWorld().spawnParticle(Particle.NOTE, location, 1, 0, 0, 0);
    }
}

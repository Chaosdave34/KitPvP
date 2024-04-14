package io.github.chaosdave34.kitpvp.cosmetics.impl

import io.github.chaosdave34.kitpvp.cosmetics.ProjectileTrail
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle

class NoteProjectileTrail : ProjectileTrail("note", "Note", 11, Material.NOTE_BLOCK) {
    override fun playEffect(location: Location) {
        location.world.spawnParticle(Particle.NOTE, location, 1, 0.0, 0.0, 0.0)
    }
}
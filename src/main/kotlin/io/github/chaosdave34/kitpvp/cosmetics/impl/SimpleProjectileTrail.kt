package io.github.chaosdave34.kitpvp.cosmetics.impl

import io.github.chaosdave34.kitpvp.cosmetics.ProjectileTrail
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle

class SimpleProjectileTrail(id: String, name: String, var particle: Particle, levelRequirement: Int, icon: Material) :
    ProjectileTrail(id, name, levelRequirement, icon) {
    override fun playEffect(location: Location) {
        location.world.spawnParticle(particle, location, 1, 0.0, 0.0, 0.0, 0.0)
    }
}
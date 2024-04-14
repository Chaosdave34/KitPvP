package io.github.chaosdave34.kitpvp.cosmetics.impl

import io.github.chaosdave34.kitpvp.cosmetics.KillEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle

class SimpleKillEffect(id: String, name: String, val particle: Particle, levelRequirement: Int, icon: Material) : KillEffect(id, name, levelRequirement, icon) {
    override fun playEffect(location: Location) {
        location.world.spawnParticle(particle, location.add(0.0, 1.0, 0.0), 1, 0.0, 0.0, 0.0, 0.0)
    }
}
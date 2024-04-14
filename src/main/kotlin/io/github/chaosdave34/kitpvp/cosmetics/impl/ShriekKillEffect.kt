package io.github.chaosdave34.kitpvp.cosmetics.impl

import io.github.chaosdave34.kitpvp.cosmetics.KillEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle

class ShriekKillEffect : KillEffect("shriek", "Shriek", 2, Material.SCULK_SHRIEKER) {
    override fun playEffect(location: Location) {
        location.world.spawnParticle(Particle.SHRIEK, location, 10, 0.0, 0.0, 0.0, 0.0, 1)
    }
}
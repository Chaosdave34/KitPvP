package io.github.chaosdave34.kitpvp.cosmetics

import org.bukkit.Location
import org.bukkit.Material

abstract class ProjectileTrail(id: String, name: String, levelRequirement: Int, icon: Material) : Cosmetic(id, name, levelRequirement, icon) {
    abstract fun playEffect(location: Location)
}

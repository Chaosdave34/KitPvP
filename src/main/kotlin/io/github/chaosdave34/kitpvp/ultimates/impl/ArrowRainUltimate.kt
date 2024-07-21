package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.Arrow
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.random.Random

class ArrowRainUltimate : Ultimate("arrow_rain", "Arrow Rain", 30, 10.0, Material.ARROW) {
    override fun getDescription() = createSimpleDescriptionAsList("Let arrows rain on your enemies.")

    override fun onAbility(player: Player): Boolean {
        val location = player.location
        val x = location.x
        val y = kotlin.math.min(110.0, location.y + 20)
        val z = location.z

        var xOffset = -15.0
        var zOffset = -15.0

        while (xOffset < 15) {
            while (zOffset < 15) {
                location.world.spawn(
                    Location(location.world, x + xOffset + Random.nextDouble(), y, z + zOffset + Random.nextDouble()),
                    Arrow::class.java
                ) { arrow ->
                    arrow.shooter = player
                    arrow.velocity = Vector(0, -1, 0)
                    arrow.pickupStatus = AbstractArrow.PickupStatus.DISALLOWED
                    arrow.isCritical = true
                    arrow.damage = 5.0
                    arrow.fireTicks = arrow.maxFireTicks
                }
                zOffset += Random.nextDouble(2.0, 4.0)
            }
            zOffset = -10.0
            xOffset += Random.nextDouble(2.0, 4.0)
        }

        return true
    }
}
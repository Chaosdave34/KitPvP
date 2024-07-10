package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import org.bukkit.Material
import org.bukkit.entity.Player

class StormUltimate: Ultimate("storm", "Storm", 30, 10.0, Material.TUBE_CORAL) {
    override fun getDescription() = createSimpleDescriptionAsList("Gather a storm for 45 seconds.")

    override fun onAbility(player: Player): Boolean {
        val world = player.world
        world.setStorm(true)
        world.weatherDuration = 45 * 20
        world.isThundering = true
        world.thunderDuration = 45 * 20

        return true
    }
}
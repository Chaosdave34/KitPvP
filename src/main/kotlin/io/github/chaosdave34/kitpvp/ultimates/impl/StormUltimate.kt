package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class StormUltimate: Ultimate("storm", "Storm", 100.0) {
    override fun getDescription(): Component = createSimpleDescription("Gather a storm for 45 seconds.")

    override fun onAbility(player: Player) {
        val world = player.world
        world.setStorm(true)
        world.weatherDuration = 45 * 20
        world.isThundering = true
        world.thunderDuration = 45 * 20
    }
}
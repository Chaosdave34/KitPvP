package io.github.chaosdave34.kitpvp.abilities.impl.poseidon

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class StormAbility : Ability("storm", "Storm", Type.SNEAK_RIGHT_CLICK, 120) {
    override fun getDescription(): List<Component> = createSimpleDescription("Gather a storm for 45 seconds.")

    override fun onAbility(player: Player): Boolean {
        val world = player.world
        world.setStorm(true)
        world.weatherDuration = 45 * 20
        world.isThundering = true
        world.thunderDuration = 45 * 20
        return true
    }
}
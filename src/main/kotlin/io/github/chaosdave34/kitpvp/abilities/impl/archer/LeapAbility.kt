package io.github.chaosdave34.kitpvp.abilities.impl.archer

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class LeapAbility : Ability("leap", "Leap", Type.RIGHT_CLICK, 10) {
    override fun getDescription(): List<Component> {
        return createSimpleDescription(
            "Leap into the direction",
            "you are looking at."
        )
    }

    override fun onAbility(player: Player): Boolean {
        val launchVector = player.eyeLocation.direction.normalize()
        launchVector.multiply(2)
        player.velocity = launchVector
        return true
    }
}
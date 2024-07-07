package io.github.chaosdave34.kitpvp.abilities.impl

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

class LeapAbility : Ability("leap", "Leap", 10, 50, Material.FEATHER) {
    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Leap into the direction you are looking at.")

    override fun onAbility(player: Player): Boolean {
        val launchVector = player.eyeLocation.direction.normalize()
        launchVector.multiply(2)
        player.velocity = launchVector
        return true
    }
}
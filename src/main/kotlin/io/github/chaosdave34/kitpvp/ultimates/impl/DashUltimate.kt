package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class DashUltimate: Ultimate("dash", "Dash", 50.0) {
    override fun getDescription(): Component {
        return createSimpleDescription("Dash forward damaging enemies.")
    }

    override fun onAbility(player: Player) { // Todo: Damage and knock back
        player.velocity = player.eyeLocation.direction.multiply(4)
    }
}
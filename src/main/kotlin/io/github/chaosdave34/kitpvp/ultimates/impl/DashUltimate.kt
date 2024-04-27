package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import org.bukkit.entity.Player

class DashUltimate: Ultimate("dash", "Dash", 50.0) {
    override fun onAbility(player: Player) {
        player.velocity = player.eyeLocation.direction.multiply(2)
    }
}
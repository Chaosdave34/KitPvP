package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class LevitateUltimate: Ultimate("levitate", "Levitate", 100.0) {
    override fun getDescription(): Component = createSimpleDescription("Let all enemies around you float into air.")

    override fun onAbility(player: Player) {
        player.getNearbyEntities(10.0, 10.0, 10.0).forEach { target ->
            if (target.checkTargetIfPlayer()) {
                target.velocity = target.velocity.setY(5)
            }
        }
    }
}
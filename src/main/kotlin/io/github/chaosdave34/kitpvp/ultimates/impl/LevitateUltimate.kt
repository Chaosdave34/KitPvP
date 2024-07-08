package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import org.bukkit.Material
import org.bukkit.entity.Player

class LevitateUltimate: Ultimate("levitate", "Levitate", 10.0, Material.SHULKER_SHELL) {
    override fun getDescription()= createSimpleDescriptionAsList("Let all enemies around you float into air.")

    override fun onAbility(player: Player): Boolean {
        player.getNearbyEntities(10.0, 10.0, 10.0).forEach { target ->
            if (target.checkTargetIfPlayer()) {
                target.velocity = target.velocity.setY(5)
            }
        }
        return true
    }
}
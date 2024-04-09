package io.github.chaosdave34.kitpvp.abilities.impl.magician

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class LevitateAbility : Ability("levitate", "Levitate", Type.RIGHT_CLICK, 20) {
    override fun getDescription(): List<Component> = createSimpleDescription("Let all enemies around you float into air.")

    override fun onAbility(player: Player): Boolean {
        player.getNearbyEntities(10.0, 10.0, 10.0).forEach { target ->
            if (target.checkTargetIfPlayer()) {
                target.velocity = target.velocity.setY(5)
            }
        }
        return true
    }
}
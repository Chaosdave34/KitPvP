package io.github.chaosdave34.kitpvp.abilities.impl.engineer

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.entities.CustomEntities
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class TurretAbility : Ability("turret", "Turret", Type.RIGHT_CLICK, 60) {
    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Deploy a mobile turret.")

    override fun onAbility(player: Player): Boolean {
        CustomEntities.TURRET.spawn(player, player.location)
        return true
    }
}
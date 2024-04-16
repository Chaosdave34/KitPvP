package io.github.chaosdave34.kitpvp.abilities.impl.vampire

import io.github.chaosdave34.kitpvp.ExtendedPlayer.Companion.from
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import net.kyori.adventure.text.Component
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class BatMorphAbility : Ability("bat_morph", "Bat Morph", Type.RIGHT_CLICK, 25) {
    override fun getDescription(): List<Component> {
        return createSimpleDescription("Morph into a bat for 10s.")
    }

    override fun onAbility(player: Player): Boolean {
        val extendedPlayer = from(player)
        extendedPlayer.morph(EntityType.BAT)

        AbilityRunnable.runTaskLater(KitPvp.INSTANCE, { player.vehicle?.removePassenger(player) }, extendedPlayer, 10 * 20)

        return true
    }
}

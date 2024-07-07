package io.github.chaosdave34.kitpvp.abilities.impl

import io.github.chaosdave34.kitpvp.ExtendedPlayer.Companion.from
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Bat
import org.bukkit.entity.Player

class BatMorphAbility : Ability("bat_morph", "Bat Morph", 25, 50, Material.BAT_SPAWN_EGG) {
    override fun getDescription(): List<Component> {
        return createSimpleDescriptionAsList("Morph into a bat for 10s.")
    }

    override fun onAbility(player: Player): Boolean {
        val extendedPlayer = from(player)
        extendedPlayer.morph(Bat::class)

        AbilityRunnable.runTaskLater(KitPvp.INSTANCE, { player.vehicle?.removePassenger(player) }, extendedPlayer, 10 * 20)

        return true
    }
}

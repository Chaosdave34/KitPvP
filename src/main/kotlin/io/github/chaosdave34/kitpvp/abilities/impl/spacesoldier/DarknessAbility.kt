package io.github.chaosdave34.kitpvp.abilities.impl.spacesoldier

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class DarknessAbility : Ability("darkness", "Darkness", Type.RIGHT_CLICK, 15) {
    override fun getDescription(): List<Component> {
        return createSimpleDescription("Shrouds the player you are looking at in a 10 block radius in darkness .")
    }

    override fun onAbility(player: Player): Boolean {
        val target = player.getTargetEntity(10)
        if (target is Player && target.checkTargetIfPlayer()) {
            target.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 5 * 20, 0))
            return true
        }
        return false
    }
}
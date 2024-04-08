package io.github.chaosdave34.kitpvp.abilities.impl.creeper

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

class ChargeAbility: Ability("charge", "Charge", Type.RIGHT_CLICK, 20) {
    override fun getDescription(): List<Component> = createSimpleDescription("Charge yourself!")

    override fun onAbility(player: Player): Boolean {
        player.world.playSound(player.location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 1f, 0f)
        player.isGlowing = true

        return true
    }
}
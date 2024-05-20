package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

class ChargeUltimate : Ultimate("charge", "Charge", 100.0) {
    override fun getDescription(): Component = createSimpleDescription("Charge yourself!")


    override fun onAbility(player: Player) {
        player.world.playSound(player.location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 1f, 0f)
        player.isGlowing = true
    }
}
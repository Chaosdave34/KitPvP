package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

class ChargeUltimate : Ultimate("charge", "Charge", 10.0, Material.BLAZE_ROD) {
    override fun getDescription() = createSimpleDescriptionAsList("Charge yourself!")


    override fun onAbility(player: Player): Boolean {
        player.world.playSound(player.location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, 1f, 0f)
        player.isGlowing = true
        return true
    }
}
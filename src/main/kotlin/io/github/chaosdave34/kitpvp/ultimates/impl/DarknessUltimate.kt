package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import net.kyori.adventure.text.Component
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class DarknessUltimate : Ultimate("darkness", "Darkness", 100.0) {
    override fun getDescription(): Component = createSimpleDescription("Shrouds all enemies in a 10 block radius in darkness.")

    override fun onAbility(player: Player) {
        player.getNearbyEntities(10.0, 10.0, 10.0).forEach { target ->
            if (target is LivingEntity)
                target.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 5 * 20, 0))
        }
    }
}
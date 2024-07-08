package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class DarknessUltimate : Ultimate("darkness", "Darkness", 10.0, Material.WARDEN_SPAWN_EGG) {
    override fun getDescription() = createSimpleDescriptionAsList("Shrouds all enemies in a 10 block radius in darkness.")

    override fun onAbility(player: Player): Boolean {
        player.getNearbyEntities(10.0, 10.0, 10.0).forEach { target ->
            if (target is LivingEntity)
                target.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 5 * 20, 0))
        }
        return true
    }
}
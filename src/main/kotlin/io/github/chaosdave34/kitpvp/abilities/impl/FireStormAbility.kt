package io.github.chaosdave34.kitpvp.abilities.impl

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin

class FireStormAbility : Ability("fire_storm", "Fire Storm", 20, 50, Material.BLAZE_POWDER) {
    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Cause a storm of fire damaging nearby enemies.")

    override fun onAbility(player: Player): Boolean {
        object : AbilityRunnable(player) {
            val delay = 10
            var counter = 0
            override fun runInGame() {
                counter += delay

                if (counter == delay * 20) cancel()

                player.getNearbyEntities(3.0, 3.0, 3.0).forEach { entity ->
                    if (entity is LivingEntity && entity.checkTargetIfPlayer()) {
                        entity.damage(2.0, DamageSource.builder(DamageType.ON_FIRE).withDirectEntity(player).withCausingEntity(player).build())
                        entity.setFireTicks(20)
                    }
                }

                val location = player.location

                for (positionCounter in 0 until 6) {
                    val position = location.clone()

                    position.add(3 * cos(positionCounter.toDouble()), 0.0, 3 * sin(positionCounter.toDouble()))

                    for (yCounter in 0 until 25) {
                        position.add(0.0, yCounter * 0.2, 0.0)
                        position.world.spawnParticle(Particle.FLAME, position, 1, 0.0, 0.0, 0.0, 0.0)
                    }
                }
            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, 10)

        return true
    }
}
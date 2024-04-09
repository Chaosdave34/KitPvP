package io.github.chaosdave34.kitpvp.abilities.impl.poseidon

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.Particle
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

class WaterBurstAbility : Ability("water_burst", "Water Burst", Type.RIGHT_CLICK, 25) {
    private val snowballMetadata = FixedMetadataValue(KitPvp.INSTANCE, id)

    override fun getDescription(): List<Component> {
        return createSimpleDescription("Spawn a water burst.")
    }

    override fun onAbility(player: Player): Boolean {
        val snowball = player.launchProjectile(Snowball::class.java)
        snowball.setMetadata("ability", snowballMetadata)
        return true
    }

    @EventHandler
    fun onSnowballImpact(event: ProjectileHitEvent) {
        val snowball = event.entity
        if (snowball is Snowball && snowball.getMetadata("ability").contains(snowballMetadata)) {
            val location = event.hitBlock?.location ?: return
            location.add(0.0, 1.0, 0.0)

            object : BukkitRunnable() {
                var counter = 0

                override fun run() {
                    counter++

                    if (counter < 5 * 20) {
                        if (counter % 5 == 0) {
                            val offset = counter / 50.0
                            location.world.spawnParticle(Particle.WATER_SPLASH, location, (10 * offset).toInt(), offset, 0.0, offset, 0.0)
                        }
                    } else if (counter < 6 * 20) {
                        for (i in 1..2)
                            location.world.spawnParticle(Particle.WATER_SPLASH, location.add(0.0, 0.25, 0.0), 50, 2.0, 0.0, 2.0, 0.0)

                        if (counter == 5 * 20) {
                            location.getNearbyLivingEntities(3.5, 5.0, 3.5).forEach { livingEntity ->
                                if (livingEntity != snowball.shooter) {
                                    livingEntity.damage(
                                        10.0,
                                        DamageSource.builder(DamageType.DROWN).withDirectEntity(livingEntity).withCausingEntity(livingEntity).build()
                                    )
                                    livingEntity.velocity = livingEntity.location.subtract(location).toVector().normalize().multiply(1.5).setY(1.5)
                                }
                            }
                        }
                    } else cancel()
                }
            }.runTaskTimer(KitPvp.INSTANCE, 0, 1)
        }
    }
}
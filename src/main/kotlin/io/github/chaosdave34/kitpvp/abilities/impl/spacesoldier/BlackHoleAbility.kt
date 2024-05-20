package io.github.chaosdave34.kitpvp.abilities.impl.spacesoldier

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.damagetype.DamageTypes
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.damage.DamageSource
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable

class BlackHoleAbility : Ability("black_hole", "Black Hole", Type.RIGHT_CLICK, 25) {
    private val snowballMetadata = FixedMetadataValue(KitPvp.INSTANCE, id)

    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Spawn a black hole.")

    override fun onAbility(player: Player): Boolean {
        val snowball = player.launchProjectile(Snowball::class.java)
        snowball.setMetadata("ability", snowballMetadata)
        return true
    }

    @EventHandler
    fun onSnowballImpact(event: ProjectileHitEvent) {
        val snowball = event.entity
        if (snowball is Snowball && snowball.getMetadata("ability").contains(snowballMetadata)) {
            val location = event.hitBlock?.location?.add(0.0, 1.0, 0.0) ?: event.hitEntity?.location ?: return
            location.add(0.0, 1.0, 0.0)

            location.world.spawnParticle(Particle.EXPLOSION_EMITTER, location, 1)
            location.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f)

            object : BukkitRunnable() {
                var i = 0
                override fun run() {
                    location.world.spawnParticle(Particle.DUST, location, 1, 0.0, 0.0, 0.0, 0.0, DustOptions(Color.BLACK, 1f))

                    for (entity in location.getNearbyEntities(5.0, 5.0, 5.0)) {
                        if (entity.checkTargetIfPlayer()) {
                            if (entity is LivingEntity) {
                                val currentVelocity = entity.velocity
                                val direction = location.clone().subtract(entity.location).toVector().setY(0).normalize()

                                val distanceSquared = location.distanceSquared(entity.location)

                                if (distanceSquared > 25) continue

                                val shooter = snowball.shooter
                                if (distanceSquared <= 2 && shooter is LivingEntity) {
                                    val damageSource = DamageSource.builder(DamageTypes.BLACK_HOLE).withDirectEntity(shooter).withCausingEntity(shooter)
                                        .withDamageLocation(location).build()
                                    entity.damage(4.0, damageSource)
                                }

                                val multiplier = 0.09 * (1 - distanceSquared / (5 * 5))

                                direction.multiply(multiplier)

                                currentVelocity.x = direction.x
                                currentVelocity.z = direction.z

                                entity.velocity = currentVelocity
                            }
                        }
                    }
                    if (i == 15 * 20) cancel()

                    i++
                }
            }.runTaskTimer(KitPvp.INSTANCE, 10, 1)
        }
    }
}
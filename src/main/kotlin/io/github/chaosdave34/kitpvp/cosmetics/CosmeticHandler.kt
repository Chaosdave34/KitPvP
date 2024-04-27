package io.github.chaosdave34.kitpvp.cosmetics

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.cosmetics.impl.NoteProjectileTrail
import io.github.chaosdave34.kitpvp.cosmetics.impl.ShriekKillEffect
import io.github.chaosdave34.kitpvp.cosmetics.impl.SimpleKillEffect
import io.github.chaosdave34.kitpvp.cosmetics.impl.SimpleProjectileTrail
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.scheduler.BukkitRunnable

class CosmeticHandler : Listener{
    var projectileTrails: MutableMap<String, ProjectileTrail> = mutableMapOf()
    private var activeProjectiles: MutableMap<Int, Int> = mutableMapOf()

    var killEffects: MutableMap<String, KillEffect> = mutableMapOf()

    companion object {
        lateinit var HEARTS_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var COMPOSTER_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var ANGRY_VILLAGER_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var BUBBLE_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var CHERRY_LEAVES_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var ELECTRIC_SPARK_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var ENCHANT_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var FLAME_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var GLOW_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var NAUTILUS_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var NOTE_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var SOUL_FIRE_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var TOTEM_OF_UNDYING_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var SNOWFLAKE_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var SOUL_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var SCULK_SOUL_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var SCULK_CHARGE_PROJECTILE_TRAIL: ProjectileTrail
        lateinit var END_ROD_PROJECTILE_TRAIL: ProjectileTrail


        lateinit var SHRIEK_KILL_EFFECT: KillEffect
        lateinit var SMOKE_KILL_EFFECT: KillEffect
        lateinit var SONIC_BOOM_KILL_EFFECT: KillEffect
        lateinit var GUST_KILL_EFFECT: KillEffect
        lateinit var SQUID_INK_KILL_EFFECT: KillEffect
        lateinit var GLOW_SQUID_INK_KILL_EFFECT: KillEffect
        lateinit var EXPLOSION_KILL_EFFECT: KillEffect
    }

    init {


        // Projectile Trails
        HEARTS_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("hearts", "Hearts", Particle.HEART, 1, Material.RED_DYE))
        COMPOSTER_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("composter", "Composter", Particle.COMPOSTER, 2, Material.COMPOSTER))
        ANGRY_VILLAGER_PROJECTILE_TRAIL =
            registerProjectileTrail(SimpleProjectileTrail("angry_villager", "Angry Villager", Particle.ANGRY_VILLAGER, 3, Material.VILLAGER_SPAWN_EGG))
        BUBBLE_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("bubble", "Bubble", Particle.BUBBLE_POP, 4, Material.WATER_BUCKET))
        CHERRY_LEAVES_PROJECTILE_TRAIL =
            registerProjectileTrail(SimpleProjectileTrail("cherry_blossom", "Cherry Blossom", Particle.CHERRY_LEAVES, 5, Material.CHERRY_LEAVES))
        ELECTRIC_SPARK_PROJECTILE_TRAIL =
            registerProjectileTrail(SimpleProjectileTrail("electric_spark", "Electric Spark", Particle.ELECTRIC_SPARK, 6, Material.LIGHTNING_ROD))
        ENCHANT_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("enchant", "Enchant", Particle.ENCHANT, 7, Material.ENCHANTING_TABLE))
        FLAME_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("flame", "Flame", Particle.SMALL_FLAME, 8, Material.TORCH))
        GLOW_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("glow", "Glow", Particle.GLOW, 9, Material.GLOW_INK_SAC))
        NAUTILUS_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("nautilus", "Nautilus", Particle.NAUTILUS, 10, Material.NAUTILUS_SHELL))
        NOTE_PROJECTILE_TRAIL = registerProjectileTrail(NoteProjectileTrail())
        SOUL_FIRE_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("soul_fire", "Soul Fire", Particle.SOUL_FIRE_FLAME, 12, Material.SOUL_LANTERN))
        TOTEM_OF_UNDYING_PROJECTILE_TRAIL =
            registerProjectileTrail(SimpleProjectileTrail("totem_of_undying", "Totem of Undying", Particle.TOTEM_OF_UNDYING, 13, Material.TOTEM_OF_UNDYING))
        SNOWFLAKE_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("snowflake", "Snowflake", Particle.SNOWFLAKE, 14, Material.SNOWBALL))
        SOUL_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("soul", "Soul", Particle.SOUL, 15, Material.SOUL_SAND))
        SCULK_SOUL_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("sculk_soul", "Sculk Soul", Particle.SCULK_SOUL, 16, Material.SCULK_CATALYST))
        SCULK_CHARGE_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("sculk_charge", "Sculk Charge", Particle.SCULK_CHARGE_POP, 17, Material.SCULK))
        END_ROD_PROJECTILE_TRAIL = registerProjectileTrail(SimpleProjectileTrail("end_rod", "End Rod", Particle.END_ROD, 18, Material.END_ROD))


        // Kill Effects
        SMOKE_KILL_EFFECT = registerKillEffect(SimpleKillEffect("smoke", "Smoke", Particle.CAMPFIRE_COSY_SMOKE, 1, Material.COAL))
        SHRIEK_KILL_EFFECT = registerKillEffect(ShriekKillEffect())
        SONIC_BOOM_KILL_EFFECT = registerKillEffect(SimpleKillEffect("sonic_boom", "Sonic Boom", Particle.SONIC_BOOM, 3, Material.WARDEN_SPAWN_EGG))
        GUST_KILL_EFFECT = registerKillEffect(SimpleKillEffect("gust", "Gust", Particle.GUST, 4, Material.STONE_SWORD))
        SQUID_INK_KILL_EFFECT = registerKillEffect(SimpleKillEffect("squid_ink", "Squid Ink", Particle.SQUID_INK, 5, Material.INK_SAC))
        GLOW_SQUID_INK_KILL_EFFECT = registerKillEffect(SimpleKillEffect("glow_squid_ink", "Glow Squid Ink", Particle.GLOW_SQUID_INK, 6, Material.GLOW_INK_SAC))
        EXPLOSION_KILL_EFFECT = registerKillEffect(SimpleKillEffect("explosion", "Explosion", Particle.EXPLOSION, 7, Material.TNT))
    }

    private fun registerProjectileTrail(projectileTrail: ProjectileTrail): ProjectileTrail {
        projectileTrails[projectileTrail.id] = projectileTrail
        return projectileTrail
    }

    private fun registerKillEffect(killEffect: KillEffect): KillEffect {
        killEffects[killEffect.id] = killEffect
        return killEffect
    }


    @EventHandler
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        val projectile = event.entity
        val shooter = event.entity.shooter
        if (shooter is Player) {
            val extendedPlayer: ExtendedPlayer = ExtendedPlayer.from(shooter)

            if (extendedPlayer.inSpawn()) return

            val projectileTrail = projectileTrails[extendedPlayer.projectileTrailId] ?: return

            val task = object : BukkitRunnable() {
                override fun run() {
                    projectileTrail.playEffect(projectile.location)

                    if (projectile is Firework) {
                        if (projectile.isDetonated) this.cancel()
                    }

                    if (!projectile.isValid || projectile.isDead) {
                        this.cancel()
                    }
                }
            }.runTaskTimer(KitPvp.INSTANCE, 0, 1)

            activeProjectiles[projectile.entityId] = task.taskId
        }

    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val taskId = activeProjectiles.remove(event.entity.entityId) ?: return
        Bukkit.getScheduler().cancelTask(taskId)
    }

    fun triggerKillEffect(killer: Player, target: Entity) {
        val extendedDamager = ExtendedPlayer.from(killer)
        killEffects[extendedDamager.killEffectId]?.playEffect(target.location)
    }

    @EventHandler
    fun onKill(event: EntityDeathEvent) {
        val entity = event.entity
        val damageEvent = entity.lastDamageCause
        if (damageEvent is EntityDamageByEntityEvent) {
            val damagerEntity: Entity = damageEvent.damager
            if (damagerEntity is Player) {
                triggerKillEffect(damagerEntity, entity)
            }
            if (damagerEntity is Projectile && damagerEntity.shooter is Player) {
                val shooter = damagerEntity.shooter
                if (shooter is Player) {
                    triggerKillEffect(shooter, entity)
                }
            }
        }
    }
}
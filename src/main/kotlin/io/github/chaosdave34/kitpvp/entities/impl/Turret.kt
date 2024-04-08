package io.github.chaosdave34.kitpvp.entities.impl

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent
import io.github.chaosdave34.ghutils.entity.CustomEntity
import io.github.chaosdave34.ghutils.utils.PDCUtils
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.events.EntityReceiveDamageByEntityEvent
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent
import io.github.chaosdave34.kitpvp.pathfindergoals.TurretRangedAttackGoal
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftMob
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class Turret : CustomEntity("turret") {
    private val turrets: MutableMap<UUID, UUID> = mutableMapOf()

    override fun spawn(player: Player, location: Location) {
        val turret = player.world.spawn<Husk>(location, Husk::class.java) { husk: Husk ->
            husk.customName(Component.text("Turret 10/10"))
            husk.isCustomNameVisible = true

            husk.setBaby()
            husk.isSilent = true

            husk.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 10.0

            husk.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 1.0

            husk.health = 10.0

            PDCUtils.setOwner(husk, player.uniqueId)

            val container = husk.persistentDataContainer
            container.set(NamespacedKey(KitPvp.INSTANCE, "custom_entity"), PersistentDataType.STRING, "turret")
        }
        turrets[player.uniqueId] = turret.uniqueId
    }

    override fun onAddToWorld(event: EntityAddToWorldEvent) {
        val entity = event.entity
        if (event.entity is Mob) {
            val nmsMob = (entity as CraftMob).handle

            val turretOwnerUUUID = PDCUtils.getOwner(entity)

            nmsMob.goalSelector.removeAllGoals { true }
            nmsMob.goalSelector.addGoal(1, RandomLookAroundGoal(nmsMob))
            nmsMob.goalSelector.addGoal(1, TurretRangedAttackGoal(nmsMob, 20, 20.0f))

            nmsMob.targetSelector.removeAllGoals { true }
            nmsMob.targetSelector.addGoal(1, NearestAttackableTargetGoal(
                nmsMob,
                net.minecraft.world.entity.player.Player::class.java, 10, true, true
            ) { livingEntity: LivingEntity -> livingEntity.uuid != turretOwnerUUUID })

            object : BukkitRunnable() {
                override fun run() {
                    entity.damage(1.0)

                    if (entity.isDead() || !entity.isValid()) this.cancel()
                }
            }.runTaskTimer(KitPvp.INSTANCE, 0, (20 * 5).toLong())
        }
    }

    @EventHandler
    fun onDamageByOwner(event: EntityReceiveDamageByEntityEvent) {
        if (event.entity is org.bukkit.entity.LivingEntity) {
            if (checkCustomEntity(event.entity)) {
                if (event.damager is Player) {
                    if (event.damager.uniqueId == PDCUtils.getOwner(event.entity)) {
                        event.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity is org.bukkit.entity.LivingEntity) {
            if (checkCustomEntity(entity)) {
                if (event.damage == Float.MAX_VALUE.toDouble()) return

                if (event.cause == EntityDamageEvent.DamageCause.FIRE || event.cause == EntityDamageEvent.DamageCause.FIRE_TICK || event.cause == EntityDamageEvent.DamageCause.DROWNING) {
                    event.isCancelled = true
                    return
                }

                event.damage = 1.0
                entity.customName(Component.text("Turret " + Math.round(entity.health) + "/10"))
            }
        }
    }

    fun performRangedAttack(mob: Mob, target: LivingEntity) {
        val rocketItem = ItemStack(Material.FIREWORK_ROCKET)
        rocketItem.editMeta(FireworkMeta::class.java) { fireworkMeta: FireworkMeta ->
            fireworkMeta.power = 5
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.BURST).build())
        }

        val d0 = target.eyeY - 1.100000023841858
        val d1 = target.x - mob.x
        val d2 = d0 - mob.eyeLocation.y
        val d3 = target.z - mob.z
        val speed = 1.6f

        val velocity = Vector(d1, d2, d3).normalize().multiply(speed)

        mob.world.spawnEntity(mob.eyeLocation, EntityType.FIREWORK, CreatureSpawnEvent.SpawnReason.CUSTOM) { entity: Entity ->
            val firework = entity as Firework
            val fireworkMeta = firework.fireworkMeta
            fireworkMeta.power = 3
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.BLACK).withColor(Color.RED).with(FireworkEffect.Type.BURST).build())
            firework.fireworkMeta = fireworkMeta

            firework.isShotAtAngle = true
            firework.ticksToDetonate = 60
            firework.shooter = mob
            firework.velocity = velocity
        }
        mob.world.playSound(mob.location, Sound.ENTITY_GENERIC_WIND_BURST, 1.0f, 0.4f)
    }

    @EventHandler
    fun onPlayerSpawn(e: PlayerSpawnEvent) {
        val p = e.player
        if (turrets.containsKey(p.uniqueId)) {
            val entity = Bukkit.getEntity(turrets[p.uniqueId]!!)
            entity?.remove()
        }
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val p = e.player
        if (turrets.containsKey(p.uniqueId)) {
            val entity = Bukkit.getEntity(turrets[p.uniqueId]!!)
            entity?.remove()
        }
    }
}
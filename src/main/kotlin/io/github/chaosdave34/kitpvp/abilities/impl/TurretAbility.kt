package io.github.chaosdave34.kitpvp.abilities.impl

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.events.EntityReceiveDamageByEntityEvent
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent
import io.github.chaosdave34.kitpvp.extensions.getPDCOwner
import io.github.chaosdave34.kitpvp.extensions.setPDCOwner
import io.github.chaosdave34.kitpvp.pathfindergoals.TurretRangedAttackGoal
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.craftbukkit.entity.CraftSnowman
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Snowman
import org.bukkit.event.EventHandler
import org.bukkit.event.block.EntityBlockFormEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

// Todo cleanup and improve code (+ fix damaging itself
class TurretAbility : Ability("turret", "Turret", Type.RIGHT_CLICK, 60) {
    private val turrets: MutableMap<UUID, UUID> = mutableMapOf()

    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Deploy a mobile turret.")

    override fun onAbility(player: Player): Boolean {

        val turret = player.world.spawn(player.location, Snowman::class.java) {
            it.isSilent = true

            it.customName(Component.text("Turret 10/10"))
            it.isCustomNameVisible = true

            it.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 0.5
            it.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = 10.0
            it.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.baseValue = 1.0
            it.isDerp

            it.health = 10.0

            it.setPDCOwner(player)

            val container = it.persistentDataContainer
            container.set(NamespacedKey(KitPvp.INSTANCE, "custom_entity"), PersistentDataType.STRING, "turret")

            val nmsSnowman = (it as CraftSnowman).handle
            val turretOwnerUUUID = it.getPDCOwner()

            nmsSnowman.goalSelector.removeAllGoals { true }
            nmsSnowman.goalSelector.addGoal(1, RandomLookAroundGoal(nmsSnowman))
            nmsSnowman.goalSelector.addGoal(1, TurretRangedAttackGoal(nmsSnowman, 20, 20.0f))
            nmsSnowman.targetSelector.removeAllGoals { true }
            nmsSnowman.targetSelector.addGoal(1, NearestAttackableTargetGoal(
                nmsSnowman,
                net.minecraft.world.entity.player.Player::class.java, 10, true, true
            ) { livingEntity: LivingEntity -> livingEntity.uuid == turretOwnerUUUID })

            object : BukkitRunnable() {
                override fun run() {
                    it.damage(1.0)

                    if (it.isDead() || !it.isValid()) {
                        turrets.remove(player.uniqueId)
                        this.cancel()
                    }
                }
            }.runTaskTimer(KitPvp.INSTANCE, 0, 20L * 5)
        }
        turrets[player.uniqueId] = turret.uniqueId

        return true
    }

    @EventHandler
    fun onDamageByOwner(event: EntityReceiveDamageByEntityEvent) {
        if (event.entity is org.bukkit.entity.LivingEntity) {
            if (checkCustomEntity(event.entity)) {
                if (event.damager is Player) {
                    if (event.damager.uniqueId == event.entity.getPDCOwner()) {
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

    @EventHandler
    fun onPlayerSpawn(event: PlayerSpawnEvent) {
        turrets[event.player.uniqueId]?.let { Bukkit.getEntity(it)?.remove() }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        turrets[event.player.uniqueId]?.let { Bukkit.getEntity(it)?.remove() }
    }

    @EventHandler
    fun onBlockForm(event: EntityBlockFormEvent) {
        if (event.entity is Snowman && checkCustomEntity(event.entity)) {
            event.isCancelled = true
        }
    }

    private fun checkCustomEntity(entity: Entity): Boolean {
        val container = entity.persistentDataContainer
        val customEntityKey = NamespacedKey(KitPvp.INSTANCE, "custom_entity")
        return if (container.has(customEntityKey)) {
            this.id == container.get(customEntityKey, PersistentDataType.STRING)
        } else {
            false
        }
    }
}
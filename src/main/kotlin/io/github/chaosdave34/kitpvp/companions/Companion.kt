package io.github.chaosdave34.kitpvp.companions

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.extensions.setPDCOwner
import io.github.chaosdave34.kitpvp.pathfindergoals.CustomFollowOwnerGoal
import io.github.chaosdave34.kitpvp.pathfindergoals.CustomMeleeAttackGoal
import io.github.chaosdave34.kitpvp.pathfindergoals.CustomOwnerHurtByTarget
import io.github.chaosdave34.kitpvp.pathfindergoals.CustomOwnerHurtTarget
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.level.Level
import org.bukkit.Location
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.util.CraftVector
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import java.util.*
import net.minecraft.world.entity.player.Player as NMSPlayer

abstract class Companion(protected val name: String, private val attackDamage: Double) {

    abstract fun createVanillaMob(world: Level): PathfinderMob

    fun createCompanion(owner: Player): org.bukkit.entity.LivingEntity {
        val companion = createVanillaMob((owner.location.world as CraftWorld).handle)
        companion.setPos(CraftVector.toNMS(owner.location.toVector()))

        companion.attributes.registerAttribute(Attributes.ATTACK_DAMAGE)
        companion.getAttribute(Attributes.ATTACK_DAMAGE)?.baseValue = attackDamage

        companion.customName = Component.literal(name)
        companion.isCustomNameVisible = true

        companion.goalSelector.removeAllGoals { true }

        companion.goalSelector.addGoal(1, FloatGoal(companion))
        companion.goalSelector.addGoal(2, CustomMeleeAttackGoal(companion, owner, 1.0, false))
        companion.goalSelector.addGoal(3, CustomFollowOwnerGoal(companion, owner, 1.0, 10.0f, 2.0f, false))

        companion.targetSelector.removeAllGoals { true }

        companion.targetSelector.addGoal(1, CustomOwnerHurtByTarget(companion, owner))
        companion.targetSelector.addGoal(2, CustomOwnerHurtTarget(companion, owner))
        companion.targetSelector.addGoal(
            3,
            NearestAttackableTargetGoal(companion, NMSPlayer::class.java, 10, true, true)
            { livingEntity: LivingEntity -> checkNearestAttackableTargetGoal(livingEntity, owner.uniqueId) }
        )

        val bukkitMob = companion.bukkitLivingEntity
        bukkitMob.setMetadata("companion", FixedMetadataValue(KitPvp.INSTANCE, true))
        bukkitMob.setPDCOwner(owner)

        return bukkitMob
    }

    private fun checkNearestAttackableTargetGoal(livingEntity: LivingEntity, ownerUUID: UUID): Boolean {
        if (livingEntity.uuid == ownerUUID) return false

        val player = livingEntity.bukkitLivingEntity
        return if (player is Player) {
            ExtendedPlayer.from(player).inGame()
        } else true
    }

    fun createDummyCompanion(location: Location): PathfinderMob {
        val companion = createVanillaMob((location.world as CraftWorld).handle)
        companion.setPos(CraftVector.toNMS(location.toVector()))
        companion.setRot(location.yaw, location.pitch)
        companion.setYHeadRot(location.yaw)
        companion.isInvulnerable = true
        companion.isNoAi = true
        companion.isSilent = true

        companion.customName = Component.literal(name)
        companion.isCustomNameVisible = true

        return companion
    }
}
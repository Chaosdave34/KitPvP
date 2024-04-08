package io.github.chaosdave34.kitpvp.abilities.impl.engineer

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent
import net.kyori.adventure.text.Component
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.Creeper
import org.bukkit.SoundCategory
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R3.util.CraftVector
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Team
import java.util.*

class ModularShieldAbility : Ability("modular_shield", "Modular Shield", Type.RIGHT_CLICK, 20) {
    private var activeShield: MutableMap<UUID, Creeper> = mutableMapOf()

    override fun getDescription(): List<Component> = createSimpleDescription("Activate your shield for 6s.")

    override fun onAbility(player: Player): Boolean {
        val creeper = Creeper(EntityType.CREEPER, (player as CraftPlayer).handle.level())
        activeShield[player.uniqueId] = creeper

        creeper.isInvisible = true
        creeper.isInvulnerable = true
        creeper.isSilent = true
        creeper.isPowered = true
        creeper.isNoAi = true

        creeper.addEffect(MobEffectInstance(MobEffects.INVISIBILITY, -1, 1, false, false, false))

        creeper.setPos(CraftVector.toNMS(player.getLocation().toVector()))

        Utils.spawnNmsEntity(creeper)

        val extendedPlayer = ExtendedPlayer.from(player)
        val team = extendedPlayer.scoreboard.registerNewTeam("creeper_shield")
        team.addEntities(player, creeper.bukkitEntity)
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)

        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 6 * 20, 0))

        AbilityRunnable.runTaskLater(KitPvp.INSTANCE, { disableShield(player) }, player, 6 * 20)

        return true
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val creeper = activeShield[player.uniqueId] ?: return

        creeper.setPos(CraftVector.toNMS(event.to.toVector()))

        val moveEntityPacket = ClientboundTeleportEntityPacket(creeper as Entity)
        Utils.sendPacketToOnlinePlayers(moveEntityPacket)
    }

    @EventHandler
    fun onSpawn(event: PlayerSpawnEvent) {
        disableShield(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        disableShield(event.player)
    }

    private fun disableShield(player: Player) {
        val creeper = activeShield.remove(player.uniqueId) ?: return
        creeper.remove(Entity.RemovalReason.KILLED)

        val removeEntitiesPacket = ClientboundRemoveEntitiesPacket(creeper.id)
        Utils.sendPacketToOnlinePlayers(removeEntitiesPacket)

        ExtendedPlayer.from(player).scoreboard.getTeam("creeper_shield")?.unregister()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player is Player) {
            if (event.isCancelled) return

            if (activeShield.contains(player.uniqueId)) {
                event.damage *= 0.6

                if (event is EntityDamageByEntityEvent && event.isCritical)
                    player.world.playSound(player, org.bukkit.Sound.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS, 1f, 0.5f)
                else
                    player.world.playSound(player, org.bukkit.Sound.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS, 1f, 0f)

            }
        }
    }
}
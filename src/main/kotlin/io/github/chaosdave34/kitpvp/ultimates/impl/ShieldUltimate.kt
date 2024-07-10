package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.Utils
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent
import io.github.chaosdave34.kitpvp.extensions.sendPackets
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.Creeper
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.SoundCategory
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.util.CraftVector
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

class ShieldUltimate: Ultimate("shield", "Shield", 30, 15.0, Material.GLOWSTONE_DUST) {
    private var activeShield: MutableMap<UUID, Creeper> = mutableMapOf()
    override fun getDescription() = createSimpleDescriptionAsList("Activate your shield for 6s reducing damage by 60%.")

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

        player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 6 * 20, 0))

        AbilityRunnable.runTaskLater(KitPvp.INSTANCE, { disableShield(player) }, player, 6 * 20)

        return true
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val creeper = activeShield[player.uniqueId] ?: return

        creeper.setPos(CraftVector.toNMS(event.to.toVector()))

        val moveEntityPacket = ClientboundTeleportEntityPacket(creeper as Entity)
        Bukkit.getOnlinePlayers().forEach { it.sendPackets(moveEntityPacket) }
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
        Bukkit.getOnlinePlayers().forEach { it.sendPackets(removeEntitiesPacket) }

        ExtendedPlayer.from(player).scoreboard.getTeam("creeper_shield")?.unregister()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player is Player) {
            if (event.isCancelled) return

            if (activeShield.contains(player.uniqueId)) {
                event.damage *= 0.4

                if (event is EntityDamageByEntityEvent && event.isCritical)
                    player.world.playSound(player, org.bukkit.Sound.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS, 1f, 0.5f)
                else
                    player.world.playSound(player, org.bukkit.Sound.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS, 1f, 0f)

            }
        }
    }
}
package io.github.chaosdave34.kitpvp.listener

import io.github.chaosdave34.ghutils.utils.PDCUtils
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.damagetype.DamageTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent

class GamePlayerDeathListener : Listener {
    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        val extendedPlayer = ExtendedPlayer.from(player)
        event.keepInventory = true

        if (extendedPlayer.inGame()) {
            extendedPlayer.incrementTotalDeaths()

            Bukkit.getScheduler().runTaskLater(KitPvp.INSTANCE, Runnable { extendedPlayer.spawn() }, 1)

            event.isCancelled = true

            // Death Messages
            val name = player.name
            val deathMessage = event.deathMessage()
            var message = if (deathMessage == null) "" else PlainTextComponentSerializer.plainText().serialize(deathMessage)

            val lastDamageEvent = player.lastDamageCause ?: return
            val damageSource = lastDamageEvent.damageSource
            val damageType = damageSource.damageType

            // Damage by Entity
            if (lastDamageEvent is EntityDamageByEntityEvent) {
                val damager: Entity = lastDamageEvent.damager

                // Kill Credits
                if (damager is Player) {
                    ExtendedPlayer.from(damager).killedPlayer(player)
                } else if (damager is Projectile && damager.shooter is Player) {
                    val killer = damager.shooter as Player
                    ExtendedPlayer.from(killer).killedPlayer(player)
                }

                // Firework
                if (damager is LightningStrike) {
                    val killer = damager.causingPlayer

                    if (killer != null) {
                        message = "$name was killed by ${killer.name} with lightning"
                        ExtendedPlayer.from(killer).killedPlayer(player)
                    }
                }
                // Turret
                else if (damager is Firework) {
                    val shooter = damager.shooter
                    if (shooter is Husk) {
                        val turretOwnerUUUID = PDCUtils.getOwner(shooter)
                        if (turretOwnerUUUID != null) {
                            val turretOwner = Bukkit.getPlayer(turretOwnerUUUID)
                            if (turretOwner != null) {
                                message = "$name was killed by ${turretOwner.name}'s turret"
                                ExtendedPlayer.from(turretOwner).killedPlayer(player)
                            }
                        }
                    }
                    // Companions
                } else if (damager.hasMetadata("companion")) {
                    val ownerUUID = PDCUtils.getOwner(damager)
                    if (ownerUUID != null) {
                        val companionOwner = Bukkit.getPlayer(ownerUUID)
                        if (companionOwner != null) {
                            message = "$name was killed by ${companionOwner.name}'s companion"
                            ExtendedPlayer.from(companionOwner).killedPlayer(player)
                        }
                    }
                }

                // Missing death messages
                // Magic Wand / Magic damage by item
                if (lastDamageEvent.cause == EntityDamageEvent.DamageCause.MAGIC)
                    message = "$name was killed by ${damager.name} using magic"
                else if (lastDamageEvent.cause == EntityDamageEvent.DamageCause.DROWNING)
                    message = "$name was drowned by ${damager.name}"

                if (damageType == DamageTypes.BLACK_HOLE)
                    message = "$name was killed by ${damager.name}'s black hole"

            } else if (damageType === DamageTypes.LAND)
                message = "$name tried to land"
            else if (damageType === DamageTypes.ESCAPE)
                message = "$name tried to escape"


            Bukkit.broadcast(
                Component.text("☠ ", NamedTextColor.RED)
                    .append(Component.text(message, NamedTextColor.GRAY))
            )
        }
    }
}
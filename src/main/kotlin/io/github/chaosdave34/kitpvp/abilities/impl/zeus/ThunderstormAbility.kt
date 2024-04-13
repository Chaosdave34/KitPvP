package io.github.chaosdave34.kitpvp.abilities.impl.zeus

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LightningStrike
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockIgniteEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class ThunderstormAbility : Ability("thunderstorm", "Thunderstorm", Type.LEFT_CLICK, 30) {
    override fun getDescription(): List<Component> {
        return createSimpleDescription("Summons Lightning around you in a 5 block radius.")
    }

    override fun onAbility(player: Player): Boolean {
        val location = player.location

        val offsetsValues = doubleArrayOf(-3.5, -1.5, 1.5, 3.5)

        val offsets: MutableList<Vector> = ArrayList()

        for (x in offsetsValues) {
            for (z in offsetsValues) {
                if (x == 0.0 && z == 0.0) continue
                offsets.add(Vector(x, -1.0, z))
            }
        }

        offsets.shuffle()

        object : BukkitRunnable() {
            val iterator: Iterator<Vector> = offsets.iterator()

            override fun run() {
                if (iterator.hasNext()) {
                    val targetLocation = location.clone().add(iterator.next())
                    location.world.spawnEntity(targetLocation, EntityType.LIGHTNING, CreatureSpawnEvent.SpawnReason.CUSTOM) { entity: Entity ->
                        val lightningStrike = (entity as LightningStrike)
                        lightningStrike.causingPlayer = player
                        lightningStrike.flashCount = 1
                        entity.setMetadata("ability", FixedMetadataValue(KitPvp.INSTANCE, id))
                    }
                } else this.cancel()
            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, 2)

        return true
    }

    @EventHandler
    fun onLightningImpact(event: EntityDamageByEntityEvent) {
        val lightningStrike = event.damager
        if (event.entity is Player && lightningStrike is LightningStrike) {
            if (lightningStrike.hasMetadata("ability")) {
                if (id == lightningStrike.getMetadata("ability")[0].value()) {
                    if (event.entity == lightningStrike.causingPlayer) {
                        event.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler
    fun onSetFire(event: BlockIgniteEvent) {
        val lightningStrike = event.ignitingEntity
        if (lightningStrike is LightningStrike) {
            if (lightningStrike.hasMetadata("ability")) {
                if (id == lightningStrike.getMetadata("ability").get(0).value()) {
                    event.isCancelled = true
                }
            }
        }
    }
}
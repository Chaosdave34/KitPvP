package io.github.chaosdave34.kitpvp.abilities.impl.zeus

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.metadata.FixedMetadataValue

class LightningAbility : Ability("lightning", "Lightning", Type.RIGHT_CLICK, 3) {
    override fun getDescription(): List<Component> = createSimpleDescription("Strikes a lightning bold at the enemy you are looking at in a 10 block radius.")

    override fun onAbility(player: Player): Boolean {
        val target = player.getTargetEntity(10)
        if (target is LivingEntity && target.checkTargetIfPlayer()) {

            val targetLocation = target.getLocation()
            targetLocation.world.spawnEntity(targetLocation, EntityType.LIGHTNING, CreatureSpawnEvent.SpawnReason.CUSTOM) { entity: Entity ->
                val lightningStrike = (entity as LightningStrike)
                lightningStrike.causingPlayer = player
                lightningStrike.flashCount = 1
                entity.setMetadata("ability", FixedMetadataValue(KitPvp.INSTANCE, id))
            }
            return true
        }
        return false
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
}
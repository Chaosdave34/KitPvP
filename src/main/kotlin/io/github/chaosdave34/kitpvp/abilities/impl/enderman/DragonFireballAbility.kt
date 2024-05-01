package io.github.chaosdave34.kitpvp.abilities.impl.enderman

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.DragonFireball
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.metadata.FixedMetadataValue

class DragonFireballAbility : Ability("dragon_fireball", "Dragon Fireball", Type.RIGHT_CLICK, 20) {
    override fun getDescription(): List<Component> = createSimpleDescription("Shoots a dragon fireball.")

    override fun onAbility(player: Player): Boolean {
        val loc = player.eyeLocation
        player.world.spawn(loc.add(loc.direction.normalize()), DragonFireball::class.java) { fireball ->
            fireball.direction = player.location.direction.multiply(1.5)
            fireball.shooter = player
            fireball.setMetadata("ability", FixedMetadataValue(KitPvp.INSTANCE, id))
        }
        return true
    }

    // Todo: fix this to just work for fireballs from this ability and not every area effect cloud
    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val areaEffectCloud = event.damager
        if (areaEffectCloud is AreaEffectCloud) {
            if (areaEffectCloud.source is Player) {
                if (event.entity.uniqueId == areaEffectCloud.ownerUniqueId) {
                    event.isCancelled = true
                }
            }
        }
    }
}
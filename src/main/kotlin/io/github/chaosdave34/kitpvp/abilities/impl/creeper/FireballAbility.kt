package io.github.chaosdave34.kitpvp.abilities.impl.creeper

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.block.Block
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.metadata.FixedMetadataValue

class FireballAbility : Ability("fireball", "Fireball", Type.RIGHT_CLICK, 10) {
    override fun getDescription(): List<Component> {
        return createSimpleDescription("Shoots a fireball.")
    }

    override fun onAbility(player: Player): Boolean {
        val loc = player.eyeLocation
        player.world.spawn(loc.add(loc.direction.normalize()), Fireball::class.java) { fireball ->
            fireball.direction = player.location.direction.multiply(1.5)
            fireball.yield = 2f
            fireball.shooter = player
            fireball.setMetadata("ability", FixedMetadataValue(KitPvp.INSTANCE, id))
        }
        return true
    }

    @EventHandler
    fun onExplosion(event: EntityExplodeEvent) {
        val fireball = event.entity
        if (fireball is Fireball) {
            if (fireball.hasMetadata("ability")) {
                if (id == fireball.getMetadata("ability")[0].value()) {
                    event.blockList().removeIf { block: Block -> !block.hasMetadata("placed_by_player") }
                }
            }
        }
    }
}
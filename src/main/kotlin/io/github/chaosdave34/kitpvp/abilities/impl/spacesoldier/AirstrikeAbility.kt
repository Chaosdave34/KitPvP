package io.github.chaosdave34.kitpvp.abilities.impl.spacesoldier

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class AirstrikeAbility : Ability("airstrike", "Airstrike", Type.RIGHT_CLICK, 10) {
    override fun getDescription(): List<Component> {
        return createSimpleDescription("Request a airstrike at the block you are looking at in a 50 block radius.")
    }

    override fun onAbility(player: Player): Boolean {
        val location = player.getTargetBlockExact(50)?.location ?: return false

        player.world.spawnParticle(Particle.DRAGON_BREATH, location, 1500, 0.0, 70.0, 0.0, 0.0)

        location.y = 110.0

        object : BukkitRunnable() {
            override fun run() {
                player.world.spawn(location, Fireball::class.java) { fireball ->
                    fireball.setMetadata("ability", FixedMetadataValue(KitPvp.INSTANCE, id))
                    fireball.yield = 4f
                    fireball.direction = Vector(0, -1, 0)
                    fireball.shooter = player
                }
            }
        }.runTaskLater(KitPvp.INSTANCE, 3 * 20)
        return true
    }

    @EventHandler
    fun onImpact(event: EntityExplodeEvent) {
        val fireball = event.entity
        if (fireball is Fireball) {
            if (fireball.hasMetadata("ability")) {
                if (id == fireball.getMetadata("ability")[0].value()) {
                    event.blockList().removeIf { block: Block -> !block.hasMetadata("placed_by_player") }

                    val world = event.entity.world
                    val location = event.location

                    world.spawnParticle(Particle.LAVA, location, 10, 1.0, 1.0, 1.0, 1.0)
                    world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 5, 1.0, 1.0, 1.0, 1.0)
                }
            }
        }
    }
}
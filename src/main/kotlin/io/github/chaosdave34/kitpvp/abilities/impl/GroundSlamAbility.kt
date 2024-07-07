package io.github.chaosdave34.kitpvp.abilities.impl

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.pow
import kotlin.math.sin

class GroundSlamAbility : Ability("ground_slam", "Ground Slam", 10, 50, Material.MACE) {

    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("If in air create a wave that damages enemies and knocks them away.")


    @Suppress("DEPRECATION")
    override fun onAbility(player: Player): Boolean {
        if (player.isOnGround) return false

        val location = player.location

        player.world.playSound(location, Sound.ENTITY_GENERIC_BIG_FALL, 1f, 0.1f)
        player.velocity = player.velocity.add(Vector(0, -2, 0))

        val ground = location.clone()
        while (!ground.block.isSolid) ground.subtract(0.0, 1.0, 0.0)
        val height = location.y - ground.blockY - 1
        location.y = ground.blockY.toDouble()

        val direction = player.location.direction
        direction.y = 0.0
        direction.normalize()

        location.add(0.0, 0.01, 0.0)

        val lines: MutableList<List<Entity>> = mutableListOf()
        for (i in 1..6) {
            val crossProductVector = direction.clone().crossProduct(Vector(0, 1, 0)).normalize()
            val newLocation = location.clone().add(crossProductVector.clone().multiply(0.5))
            val locations = listOf(newLocation.clone(), newLocation.clone().add(crossProductVector), newLocation.clone().subtract(crossProductVector))

            val blockDisplays: MutableList<Entity> = mutableListOf()

            for (blockLocation in locations) {
                if (blockLocation.block.isEmpty) blockLocation.subtract(0.0, 1.0, 0.0)
                if (blockLocation.block.isEmpty) continue

                blockLocation.pitch = 0f
                val blockDisplay = blockLocation.world.spawn(blockLocation, BlockDisplay::class.java) { it.block = blockLocation.block.blockData }
                blockDisplays.add(blockDisplay)
            }
            lines.add(blockDisplays)
            location.add(direction)
        }

        object : BukkitRunnable() {
            var i = 1
            override fun run() {
                for ((lineNumber, line) in lines.withIndex()) {
                    val difference = lineNumber * 2
                    if (i in difference..10 + difference) {
                        line.forEach { blockDisplay: Entity ->
                            val y = sin((i - difference) * Math.PI * 0.2)
                            blockDisplay.teleport(blockDisplay.location.add(0.0, y * 0.2, 0.0))

                            blockDisplay.getNearbyEntities(1.0, 2.0, 1.0).forEach { entity ->
                                if (entity is LivingEntity && entity != player) {
                                    entity.damage(5.0, player)
                                    val distance = entity.location.subtract(blockDisplay.location).toVector()
                                    entity.knockback(2.0, distance.x, distance.z)
                                }
                            }
                        }
                    }

                    if (i == 10 + difference) {
                        line.forEach { it.remove() }
                    }
                }
                i++
            }
        }.runTaskTimer(KitPvp.INSTANCE, (height * 5.0.pow(1 / height)).toLong(), 1)

        return true
    }
}
package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

class TornadoUltimate: Ultimate("tornado", "Tornado", 100.0) {
    override fun getDescription(): Component = createSimpleDescription("Create a tornado pulling in enemies towards you for 5s.")

    override fun onAbility(player: Player) {
        object : AbilityRunnable(player) {
            override fun runInGame() {
                player.getNearbyEntities(4.0, 4.0, 4.0).forEach { entity ->
                    val pullInVelocity = entity.location.toVector().subtract(player.location.toVector()).normalize().multiply(-1.0)

                    entity.velocity = pullInVelocity
                }

                if (i % 5 == 0) player.world.playSound(player, Sound.ENTITY_BREEZE_INHALE, SoundCategory.PLAYERS, 1f, 0f)


                if (i == 5 * 20) cancel()
            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, 1)
    }
}
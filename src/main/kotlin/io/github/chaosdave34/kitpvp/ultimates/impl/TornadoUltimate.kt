package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player

class TornadoUltimate: Ultimate("tornado", "Tornado", 10.0, Material.WIND_CHARGE) {
    override fun getDescription() = createSimpleDescriptionAsList("Create a tornado pulling in enemies towards you for 5s.")

    override fun onAbility(player: Player): Boolean {
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

        return true
    }
}
package io.github.chaosdave34.kitpvp.abilities.impl.tank

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player


class TornadoAbility : Ability("tornado", "Tornado", Type.RIGHT_CLICK, 15) {
    override fun getDescription(): List<Component> = createSimpleDescription("Create a tornado pulling in enemies towards you for 5s.")

    override fun onAbility(player: Player): Boolean {
        object : AbilityRunnable(player) {
            override fun runInGame() {
                player.getNearbyEntities(4.0, 4.0, 4.0).forEach { entity ->
                    val pullInVelocity = entity.location.subtract(player.location).toVector().normalize().multiply(-1)

                    entity.velocity = pullInVelocity
                }

                if (i % 5 == 0) player.world.playSound(player, Sound.ENTITY_BREEZE_INHALE, SoundCategory.PLAYERS, 1f, 0f)


                if (i == 5 * 20) cancel()
            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, 1)

        return true
    }
}
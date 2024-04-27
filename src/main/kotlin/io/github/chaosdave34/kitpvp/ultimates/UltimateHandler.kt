package io.github.chaosdave34.kitpvp.ultimates

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent
import io.github.chaosdave34.kitpvp.ultimates.impl.DashUltimate
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import java.util.*
import kotlin.math.min

class UltimateHandler : Listener {
    private val damageCollected: MutableMap<UUID, Double> = mutableMapOf()

    companion object {
        lateinit var DASH: Ultimate
    }

    init {
        DASH = registerUltimate(DashUltimate())
    }

    private fun registerUltimate(ultimate: Ultimate): Ultimate {
        Utils.registerEvents(ultimate)
        return ultimate
    }

    fun getProgress(player: Player, ultimate: Ultimate): Float {
        val progress = (damageCollected[player.uniqueId] ?: 0.0) / ultimate.damageRequired
        return min(0.99f, progress.toFloat())
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        val player = event.player
        val extendedPlayer = ExtendedPlayer.from(player)

        if (extendedPlayer.gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {
            val ultimate = extendedPlayer.getSelectedKitsKit().getUltimate() ?: return

            if ((damageCollected[player.uniqueId] ?: 0.0) >= ultimate.damageRequired) {
                ultimate.onAbility(player)

                damageCollected[player.uniqueId] = 0.0
                player.exp = 0f
            }
        }
    }

    @EventHandler
    fun onDealDamage(event: EntityDealDamageEvent) {
        val player = event.damager
        if (player is Player) {
            val extendedPlayer = ExtendedPlayer.from(player)
            if (extendedPlayer.gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {

                val currentDamage = damageCollected[player.uniqueId] ?: 0.0
                val ultimate = extendedPlayer.getSelectedKitsKit().getUltimate() ?: return


                if (currentDamage < ultimate.damageRequired) {
                    val newDamage = currentDamage + event.damage

                    damageCollected[player.uniqueId] = newDamage

                    val progress = ultimate.getProgress(player)
                    player.exp = progress

                    if (newDamage >= ultimate.damageRequired) {
                        val availableMessage: Component = Component.text("Your ", NamedTextColor.GREEN)
                            .append(Component.text("Ultimate ", NamedTextColor.GOLD))
                            .append(Component.text("is now available.", NamedTextColor.GREEN))
                        player.sendMessage(availableMessage)
                    }
                }
            }
        }
    }
}
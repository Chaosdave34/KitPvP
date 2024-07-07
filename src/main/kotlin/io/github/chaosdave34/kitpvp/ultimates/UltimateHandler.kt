package io.github.chaosdave34.kitpvp.ultimates

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.Utils
import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent
import io.github.chaosdave34.kitpvp.ultimates.impl.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import java.util.*
import kotlin.math.min

// Todo: Update
class UltimateHandler : Listener {
    private val damageCollected: MutableMap<UUID, Double> = mutableMapOf()

    companion object {
        lateinit var DASH: Ultimate
        lateinit var ARROW_RAIN: Ultimate
        lateinit var CHARGE: Ultimate
        lateinit var SHIELD: Ultimate
        lateinit var LEVITATE: Ultimate
        lateinit var STORM: Ultimate
        lateinit var DARKNESS: Ultimate
        lateinit var TORNADO: Ultimate
        lateinit var RAGE: Ultimate
    }

    init {
        DASH = registerUltimate(DashUltimate())
        ARROW_RAIN = registerUltimate(ArrowRainUltimate())
        CHARGE = registerUltimate(ChargeUltimate())
        SHIELD = registerUltimate(ShieldUltimate())
        LEVITATE = registerUltimate(LevitateUltimate())
        STORM = registerUltimate(StormUltimate())
        DARKNESS = registerUltimate(DarknessUltimate())
        TORNADO = registerUltimate(TornadoUltimate())
        RAGE = registerUltimate(RageUltimate())
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

//        if (extendedPlayer.gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {
//            val ultimate = extendedPlayer.getSelectedKitsKit().getUltimate() ?: return
//
//            if ((damageCollected[player.uniqueId] ?: 0.0) >= ultimate.damageRequired) {
//                ultimate.onAbility(player)
//
//                damageCollected[player.uniqueId] = 0.0
//                player.exp = 0f
//            }
//        }
    }

    @EventHandler
    fun onDealDamage(event: EntityDealDamageEvent) {
        val player = event.damager
        if (player is Player) {
            val extendedPlayer = ExtendedPlayer.from(player)
            if (extendedPlayer.gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {

                val currentDamage = damageCollected[player.uniqueId] ?: 0.0
//                val ultimate = extendedPlayer.getSelectedKitsKit().getUltimate() ?: return
//
//
//                if (currentDamage < ultimate.damageRequired) {
//                    val newDamage = currentDamage + event.damage
//
//                    damageCollected[player.uniqueId] = newDamage
//
//                    val progress = ultimate.getProgress(player)
//                    player.exp = progress
//
//                    if (newDamage >= ultimate.damageRequired) {
//                        val availableMessage: Component = Component.join(
//                            JoinConfiguration.spaces(),
//                            Component.text("Your", NamedTextColor.GREEN),
//                            Component.text("Ultimate", NamedTextColor.GOLD),
//                            Component.text(ultimate.name, NamedTextColor.GRAY, TextDecoration.ITALIC),
//                            Component.text("is now available. Press", NamedTextColor.GREEN),
//                            Component.keybind("key.drop", NamedTextColor.GOLD),
//                            Component.text("to activate.", NamedTextColor.GREEN),
//                        ).hoverEvent(ultimate.getDescription())
//
//                        player.sendMessage(availableMessage)
//                    }
//                }
            }
        }
    }
}
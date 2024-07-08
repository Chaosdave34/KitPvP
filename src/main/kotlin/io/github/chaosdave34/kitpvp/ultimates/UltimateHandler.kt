package io.github.chaosdave34.kitpvp.ultimates

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.Utils
import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent
import io.github.chaosdave34.kitpvp.ultimates.impl.*
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import java.util.*

class UltimateHandler : Listener {
    val ultimates: MutableMap<String, Ultimate> = mutableMapOf()
    val damageStacksCollected: MutableMap<UUID, Double> = mutableMapOf()

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
        ultimates[ultimate.id] = ultimate
        return ultimate
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return

        if (ExtendedPlayer.from(player).gameState != ExtendedPlayer.GameState.KITS_IN_GAME) return

        val ultimateId = item.persistentDataContainer.get(NamespacedKey(KitPvp.INSTANCE, "ultimate"), PersistentDataType.STRING)
        ultimates[ultimateId]?.handleUltimate(player)
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        val player = event.player
        val item = event.itemDrop

        if (ExtendedPlayer.from(player).gameState != ExtendedPlayer.GameState.KITS_IN_GAME) return

        val ultimateId = item.persistentDataContainer.get(NamespacedKey(KitPvp.INSTANCE, "ultimate"), PersistentDataType.STRING)
        ultimates[ultimateId]?.handleUltimate(player)
    }

    @EventHandler
    fun onDealDamage(event: EntityDealDamageEvent) {
        val player = event.damager
        if (player is Player) {
            val extendedPlayer = ExtendedPlayer.from(player)
            if (extendedPlayer.gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {

                val currentStacks = damageStacksCollected[player.uniqueId] ?: 0.0
                val newStacks = currentStacks + (event.damage / 10)
                damageStacksCollected[player.uniqueId] = newStacks
            }
        }
    }
}
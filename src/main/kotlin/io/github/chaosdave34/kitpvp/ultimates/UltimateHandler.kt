package io.github.chaosdave34.kitpvp.ultimates

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.Utils
import io.github.chaosdave34.kitpvp.ultimates.impl.*
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class UltimateHandler : Listener {
    val ultimates: MutableMap<String, Ultimate> = mutableMapOf()

    init {
        registerUltimate(DashUltimate())
        registerUltimate(ArrowRainUltimate())
        registerUltimate(ChargeUltimate())
        registerUltimate(ShieldUltimate())
        registerUltimate(LevitateUltimate())
        registerUltimate(StormUltimate())
        registerUltimate(DarknessUltimate())
        registerUltimate(TornadoUltimate())
        registerUltimate(RageUltimate())
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
}
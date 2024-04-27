package io.github.chaosdave34.kitpvp.ultimates

import io.github.chaosdave34.kitpvp.KitPvp
import org.bukkit.entity.Player
import org.bukkit.event.Listener

abstract class Ultimate(val id: String, val name: String, val damageRequired: Double) : Listener {
    abstract fun onAbility(player: Player)

    fun getProgress(player: Player): Float {
        return KitPvp.INSTANCE.ultimateHandler.getProgress(player, this)
    }
}
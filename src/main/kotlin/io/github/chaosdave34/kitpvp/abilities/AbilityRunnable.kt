package io.github.chaosdave34.kitpvp.abilities

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.ExtendedPlayer.Companion.from
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

abstract class AbilityRunnable(val extendedPlayer: ExtendedPlayer) : BukkitRunnable() {
    constructor(player: Player) : this(from(player))

    private val gameType: ExtendedPlayer.GameType = extendedPlayer.currentGame
    private val deathCount: Int = extendedPlayer.getTotalDeaths(gameType)

    override fun run() {
        if (!extendedPlayer.inGame()) cancel()
        if (gameType != extendedPlayer.currentGame) cancel()
        if (deathCount != extendedPlayer.getTotalDeaths(gameType)) cancel()
        runInGame()
    }

    abstract fun runInGame()

    companion object {
        @JvmStatic
        fun runTaskLater(plugin: Plugin, task: Runnable, extendedPlayer: ExtendedPlayer, delay: Long): BukkitTask {
            return object : AbilityRunnable(extendedPlayer) {
                override fun runInGame() {
                    task.run()
                }
            }.runTaskLater(plugin, delay)
        }

        @JvmStatic
        fun runTaskLater(plugin: Plugin, task: Runnable, player: Player, delay: Long): BukkitTask {
            return runTaskLater(plugin, task, from(player), delay)
        }
    }


}
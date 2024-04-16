package io.github.chaosdave34.kitpvp.abilities

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*

abstract class AbilityRunnable(val uuid: UUID) : BukkitRunnable() {
    constructor(player: Player) : this(player.uniqueId)
    constructor(extendedPlayer: ExtendedPlayer) : this(extendedPlayer.uuid)

    private val gameType: ExtendedPlayer.GameType = ExtendedPlayer.from(uuid).currentGame
    private val deathCount: Int = ExtendedPlayer.from(uuid).getTotalDeaths(gameType)

    protected var i = 0

    override fun run() {
        val extendedPlayer = ExtendedPlayer.from(uuid)
        if (extendedPlayer.getPlayer() == null) return
        if (!extendedPlayer.inGame()) cancel()
        if (gameType != extendedPlayer.currentGame) cancel()
        if (deathCount != extendedPlayer.getTotalDeaths(gameType)) cancel()
        runInGame()
        i++
    }

    abstract fun runInGame()

    companion object {
        fun runTaskLater(plugin: Plugin, task: Runnable, extendedPlayer: ExtendedPlayer, delay: Long): BukkitTask {
            return object : AbilityRunnable(extendedPlayer) {
                override fun runInGame() {
                    task.run()
                }
            }.runTaskLater(plugin, delay)
        }

        fun runTaskLater(plugin: Plugin, task: Runnable, player: Player, delay: Long): BukkitTask {
            return runTaskLater(plugin, task, ExtendedPlayer.from(player), delay)
        }
    }
}
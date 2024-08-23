package io.github.chaosdave34.kitpvp.playerlist

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import kotlin.math.min

class PlayerListHandler {

    fun sendInitial(target: Player) {
        val onlinePlayers = Bukkit.getOnlinePlayers().sortedBy { it.name }
        val playerAmount = onlinePlayers.size

        onlinePlayers.forEach {
            it.unlistPlayer(target)
            target.unlistPlayer(it)
        }

        // Players Title 1
        var i = 0
        PlayerListStaticEntry(target, Component.text("        Players:        ").decorate(TextDecoration.BOLD), i).create()
        val list1 = onlinePlayers.subList(0, min(playerAmount, 19))

        for (onlinePlayer in list1) {
            i++
            PlayerListPlayerEntry(target, onlinePlayer, i).create()
        }

        for (j in 1..(19 - list1.size)) {
            i++
            PlayerListEmptyEntry(target, i).create()
        }

        // Players Title 2
        i++
        PlayerListStaticEntry(target, Component.text("        Players:        ").decorate(TextDecoration.BOLD), i).create()
        if (onlinePlayers.size > 19) {
            val list2 = onlinePlayers.subList(19, min(onlinePlayers.size, 38))

            for (onlinePlayer in list2) {
                i++
                PlayerListPlayerEntry(target, onlinePlayer, i).create()
            }

            for (j in 1..(19 - list2.size)) {
                i++
                PlayerListEmptyEntry(target, i).create()
            }
        }
        for (j in 1..19) {
            i++
            PlayerListEmptyEntry(target, i).create()
        }

        // Info
        i++
        PlayerListStaticEntry(target, Component.text("         Info:         ").decorate(TextDecoration.BOLD), i).create()
        for (j in 1..19) {
            i++
            PlayerListEmptyEntry(target, i).create()
        }

        // Stats
        i++
        PlayerListStaticEntry(target, Component.text("         Stats:         ").decorate(TextDecoration.BOLD), i).create()
        for (j in 1..19) {
            i++
            PlayerListEmptyEntry(target, i).create()
        }
    }
}
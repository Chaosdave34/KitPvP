package io.github.chaosdave34.kitpvp.commands

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.ExtendedPlayer.Companion.from
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GommeModeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val extendedPlayer: ExtendedPlayer = from(sender)
            extendedPlayer.gameState = ExtendedPlayer.GameState.DEBUG
            sender.gameMode = GameMode.CREATIVE
            return true
        }
        return false
    }
}
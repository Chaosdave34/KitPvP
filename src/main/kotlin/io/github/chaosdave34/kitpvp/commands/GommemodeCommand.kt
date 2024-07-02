package io.github.chaosdave34.kitpvp.commands

import com.mojang.brigadier.Command
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.GameMode
import org.bukkit.entity.Player

object GommemodeCommand {
    fun register(commands: Commands) {
        commands.register(
            Commands.literal("gommemode")
                .requires { it.sender.isOp }
                .requires { it.sender is Player }
                .executes { enterGommemode(it.source) }
                .build()
        )
    }

    private fun enterGommemode(source: CommandSourceStack): Int {
        val player = source.sender as Player
        val extendedPlayer = ExtendedPlayer.from(player)
        extendedPlayer.gameState = ExtendedPlayer.GameState.DEBUG
        player.gameMode = GameMode.CREATIVE

        return Command.SINGLE_SUCCESS
    }
}
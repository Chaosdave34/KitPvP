package io.github.chaosdave34.kitpvp.commands

import com.mojang.brigadier.Command
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

object SpawnCommand {
    fun register(commands: Commands) {
        commands.register(
            Commands.literal("spawn")
                .requires { it.sender is Player }
                .executes { spawn(it.source) }
                .build()
        )
    }

    private fun spawn(source: CommandSourceStack): Int {
        val player = source.sender as Player
        val extendedPlayer = ExtendedPlayer.from(player)

        if (extendedPlayer.combatCooldown == 0)
            extendedPlayer.spawn()
        else
            player.sendMessage(Component.text("You are still in combat for ${extendedPlayer.combatCooldown}s!", NamedTextColor.RED))

        return Command.SINGLE_SUCCESS
    }
}
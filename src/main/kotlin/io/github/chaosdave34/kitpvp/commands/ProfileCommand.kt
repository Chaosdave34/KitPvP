package io.github.chaosdave34.kitpvp.commands

import com.mojang.brigadier.Command
import io.github.chaosdave34.kitpvp.extensions.openGui
import io.github.chaosdave34.kitpvp.guis.Guis
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import org.bukkit.entity.Player

object ProfileCommand {
    fun registerCommand(commands: Commands) {
        commands.register(
            Commands.literal("profile")
                .requires { it.sender is Player }
                .then(
                    Commands.argument("target", ArgumentTypes.player())
                        .executes { viewProfile(it.source, it.getArgument("target", PlayerSelectorArgumentResolver::class.java)) }
                )
                .build()
        )
    }

    private fun viewProfile(source: CommandSourceStack, targets: PlayerSelectorArgumentResolver): Int {
        targets.resolve(source).forEach { target ->
            (source.sender as Player).openGui(Guis.PROFILE, target)
        }
        return Command.SINGLE_SUCCESS
    }
}
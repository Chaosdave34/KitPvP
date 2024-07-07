package io.github.chaosdave34.kitpvp.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import net.minecraft.commands.arguments.EntityArgument

object AddExperienceCommand {
    fun register(commands: Commands) {
        commands.register(
            Commands.literal("add-experience")
                .requires { it.sender.isOp }
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes {
                                    addExperience(
                                        it.source,
                                        it.getArgument("targets", PlayerSelectorArgumentResolver::class.java),
                                        it.getArgument("amount", Int::class.java)
                                    )
                                }
                        )
                )
                .build()
        )
    }

    private fun addExperience(source: CommandSourceStack, targets: PlayerSelectorArgumentResolver, amount: Int): Int {
        val resolvedTargets = targets.resolve(source)
        if (resolvedTargets.isEmpty()) throw EntityArgument.NO_PLAYERS_FOUND.create()

        resolvedTargets.forEach { target ->
            ExtendedPlayer.from(target).addExperiencePoints(amount)
        }
        return Command.SINGLE_SUCCESS
    }

}
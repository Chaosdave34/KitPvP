package io.github.chaosdave34.kitpvp.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import net.minecraft.commands.arguments.EntityArgument
import org.bukkit.entity.Player

object BountyCommand {
    fun register(commands: Commands) {
        commands.register(
            Commands.literal("bounty")
                .requires { it.sender is Player }
                .then(
                    Commands.argument("target", ArgumentTypes.player())
                        .then(
                            Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes {
                                    bounty(
                                        it.source,
                                        it.getArgument("target", PlayerSelectorArgumentResolver::class.java),
                                        it.getArgument("amount", Int::class.java)
                                    )
                                }
                        )
                )
                .build()
        )
    }

    private fun bounty(source: CommandSourceStack, targets: PlayerSelectorArgumentResolver, amount: Int): Int {
        targets.resolve(source).forEach { target ->
            val player = source.sender as Player

            if (target == player) {
                throw SimpleCommandExceptionType { "You cant place a bounty on yourself!" }.create()
            }

            val extendedPlayer = ExtendedPlayer.from(player)
            if (extendedPlayer.purchase(amount)) {
                player.sendMessage("You placed a bounty of $amount coins on ${target.name}.")

                return Command.SINGLE_SUCCESS
            }
            throw SimpleCommandExceptionType { "You dont have enough coins!" }.create()
        }

        throw EntityArgument.NO_PLAYERS_FOUND.create()
    }
}
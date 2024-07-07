package io.github.chaosdave34.kitpvp.commands

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.SignedMessageResolver
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.text.Component
import net.minecraft.commands.arguments.EntityArgument

object MsgCommand {
    fun register(commands: Commands) {
        commands.register(
            Commands.literal("msg")
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("message", ArgumentTypes.signedMessage())
                                .executes {
                                    message(
                                        it.source,
                                        it.getArgument("targets", PlayerSelectorArgumentResolver::class.java),
                                        it.getArgument("message", SignedMessageResolver::class.java).resolveSignedMessage("message", it).join()
                                    )
                                }
                        )
                )
                .build(),
            listOf("w", "tell")
        )
    }

    private fun message(source: CommandSourceStack, targets: PlayerSelectorArgumentResolver, message: SignedMessage): Int {
        val sender = source.sender
        val resolvedTargets = targets.resolve(source)
        if (resolvedTargets.isEmpty()) throw EntityArgument.NO_PLAYERS_FOUND.create()

        resolvedTargets.forEach { target ->
            sender.sendMessage(Component.text("To ${target.name}: ${message.message()}"))
            target.sendMessage(Component.text("From ${sender.name}: ${message.message()}"))
        }

        return Command.SINGLE_SUCCESS
    }
}
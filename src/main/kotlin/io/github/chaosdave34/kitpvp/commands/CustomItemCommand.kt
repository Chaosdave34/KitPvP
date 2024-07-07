package io.github.chaosdave34.kitpvp.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.items.CustomItem
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import java.util.concurrent.CompletableFuture

object CustomItemCommand {
    fun register(commands: Commands) {
        commands.register(
            Commands.literal("custom-item")
                .requires { it.sender.isOp }
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("item", CustomItemArgumentType())
                                .executes {
                                    customItem(
                                        it.source,
                                        it.getArgument("target", PlayerSelectorArgumentResolver::class.java),
                                        it.getArgument("item", CustomItem::class.java)
                                    )
                                }
                                .then(
                                    Commands.argument("count", IntegerArgumentType.integer(1))
                                        .executes {
                                            customItem(
                                                it.source,
                                                it.getArgument("target", PlayerSelectorArgumentResolver::class.java),
                                                it.getArgument("item", CustomItem::class.java),
                                                it.getArgument("count", Int::class.java)
                                            )
                                        }
                                )
                        )
                )
                .build()
        )
    }

    private fun customItem(source: CommandSourceStack, targets: PlayerSelectorArgumentResolver, item: CustomItem, count: Int = 1): Int {
        val resolvedTargets = targets.resolve(source)
        if (resolvedTargets.isEmpty()) throw EntityArgument.NO_PLAYERS_FOUND.create()

        resolvedTargets.forEach { target ->

            for (i in 1..count) {
                target.inventory.addItem(item.build(1))
            }

            val message = listOf(
                Component.text("Gave $count"),
                Component.text("[${item.name}]").hoverEvent(item.build(1).asHoverEvent()),
                Component.text("to"),
                Component.selector(target.name).hoverEvent(target.asHoverEvent())
            )

            source.sender.sendMessage(Component.join(JoinConfiguration.spaces(), message))
        }

        return Command.SINGLE_SUCCESS
    }

    class CustomItemArgumentType : CustomArgumentType<CustomItem, String> {
        override fun parse(reader: StringReader): CustomItem {
            val argBeginning = reader.cursor

            val input = reader.readString().lowercase()
            val customItem = KitPvp.INSTANCE.customItemHandler.customItems[input]

            reader.cursor = argBeginning
            return customItem ?: throw SimpleCommandExceptionType { "Unknown custom item '$input'" }.createWithContext(reader)
        }

        override fun getNativeType(): ArgumentType<String> {
            return StringArgumentType.word()
        }

        override fun <S> listSuggestions(context: CommandContext<S>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
            return SharedSuggestionProvider.suggest(KitPvp.INSTANCE.customItemHandler.customItems.keys, builder)
        }

    }
}
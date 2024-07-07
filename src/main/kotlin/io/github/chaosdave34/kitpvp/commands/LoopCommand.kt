package io.github.chaosdave34.kitpvp.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.LongArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.chaosdave34.kitpvp.KitPvp
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import net.kyori.adventure.text.Component
import net.minecraft.commands.SharedSuggestionProvider
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.CompletableFuture

object LoopCommand {
    fun register(commands: Commands) {
        commands.register(
            Commands.literal("loop")
                .requires { it.sender.isOp }
                .then(
                    Commands.argument("count", IntegerArgumentType.integer(1, 100))
                        .then(
                            Commands.argument("period", LongArgumentType.longArg(0))
                                .then(
                                    Commands.argument("command", StringArgumentType.greedyString())
                                        .suggests(CommandSuggestion())
                                        .executes {
                                            loop(
                                                it.source,
                                                it.getArgument("count", Int::class.java),
                                                it.getArgument("period", Long::class.java),
                                                it.getArgument("command", String::class.java)
                                            )
                                        }
                                )
                        )
                )
                .build()
        )
    }

    private fun loop(source: CommandSourceStack, count: Int, period: Long, command: String): Int {
        object : BukkitRunnable() {
            var i = 0
            override fun run() {
                if (i < count) Bukkit.getServer().dispatchCommand(source.sender, command)
                else {
                    source.sender.sendMessage(Component.text("Done!"))
                    this.cancel()
                }
                i++
            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, period)
        return Command.SINGLE_SUCCESS
    }

    private class CommandSuggestion : SuggestionProvider<CommandSourceStack> {
        override fun getSuggestions(context: CommandContext<CommandSourceStack>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
            return SharedSuggestionProvider.suggest(Bukkit.getCommandMap().knownCommands.keys, builder)
        }

    }
}
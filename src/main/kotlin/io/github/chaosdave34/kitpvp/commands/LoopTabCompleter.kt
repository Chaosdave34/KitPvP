package io.github.chaosdave34.kitpvp.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class LoopTabCompleter : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        val completions: MutableList<String> = mutableListOf()
        if (args.size == 3) {
            StringUtil.copyPartialMatches(args[2], Bukkit.getCommandMap().knownCommands.keys, completions)
            return completions
        }
        return completions
    }
}
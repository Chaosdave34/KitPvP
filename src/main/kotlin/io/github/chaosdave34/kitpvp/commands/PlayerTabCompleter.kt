package io.github.chaosdave34.kitpvp.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class PlayerTabCompleter : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        val completions: MutableList<String> = mutableListOf()
        if (args.size == 1) {
            StringUtil.copyPartialMatches(args[0], Bukkit.getOnlinePlayers().stream().map { obj: Player -> obj.name }.toList(), completions)
            return completions
        }
        return completions
    }
}
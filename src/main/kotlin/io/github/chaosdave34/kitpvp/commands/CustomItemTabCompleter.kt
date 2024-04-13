package io.github.chaosdave34.kitpvp.commands

import io.github.chaosdave34.kitpvp.KitPvp
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class CustomItemTabCompleter : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        val completions: MutableList<String> = mutableListOf()
        if (args.size == 1) {
            StringUtil.copyPartialMatches(args[0], KitPvp.INSTANCE.customItemHandler.customItems.keys, completions)
            return completions
        }
        return completions
    }
}
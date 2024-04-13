package io.github.chaosdave34.kitpvp.commands

import io.github.chaosdave34.kitpvp.ExtendedPlayer.Companion.from
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class AddExperienceCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size != 2) return false

        val player = Bukkit.getPlayer(args[0]) ?: return false
        val amount = args[1].toIntOrNull() ?: return false

        if (amount < 1) return false
        from(player).addExperiencePoints(amount)

        return true
    }
}
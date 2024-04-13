package io.github.chaosdave34.kitpvp.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MessageCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size < 2) return false

        val target = Bukkit.getPlayer(args[0])
        if (target == null) {
            sender.sendMessage(Component.text("No player was found!", NamedTextColor.RED))
            return true
        }

        val message = listOf(*args).subList(1, args.size).joinToString(" ")
        val targetMessage = "From ${sender.name}: $message"
        target.sendMessage(Component.text(targetMessage, NamedTextColor.GRAY))

        val senderMessage = "To ${target.name}: $message"
        sender.sendMessage(Component.text(senderMessage, NamedTextColor.GRAY))

        return true
    }
}
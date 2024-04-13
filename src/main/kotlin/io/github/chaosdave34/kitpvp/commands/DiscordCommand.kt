package io.github.chaosdave34.kitpvp.commands

import io.github.chaosdave34.kitpvp.KitPvp
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class DiscordCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val discordLink = KitPvp.INSTANCE.config["discord"]
        sender.sendMessage(Component.text("Discord: $discordLink"))
        return true
    }
}
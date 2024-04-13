package io.github.chaosdave34.kitpvp.commands

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.ExtendedPlayer.Companion.from
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val extendedPlayer: ExtendedPlayer = from(sender)

            if (extendedPlayer.combatCooldown == 0)
                extendedPlayer.spawn()
            else
                sender.sendMessage(Component.text("You are still in combat for ${extendedPlayer.combatCooldown}s!", NamedTextColor.RED))

            return true
        }
        return false
    }
}
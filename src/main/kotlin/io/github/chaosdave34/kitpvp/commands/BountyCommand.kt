package io.github.chaosdave34.kitpvp.commands

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.ExtendedPlayer.Companion.from
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BountyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size != 2) return false

        if (sender is Player) {
            // Get target player
            val target = Bukkit.getPlayer(args[0])
            if (target == null) {
                sender.sendMessage(Component.text("The Player could not be found."))
                return true
            }

            if (target == sender) {
                sender.sendMessage(Component.text("You can't place a bounty on yourself."))
                return true
            }

            // Get bounty coin amount
            val amount = args[1].toIntOrNull() ?: -1

            if (amount <= 0) {
                sender.sendMessage(Component.text("Invalid amount of coins."))
                return true
            }

            // Check and purchase
            val extendedPlayer: ExtendedPlayer = from(sender)
            if (extendedPlayer.purchase(amount)) {
                sender.sendMessage("You placed a bounty of $amount coins on ${target.name}.")

                from(target).receivedBounty(amount)
            } else {
                sender.sendMessage("You don't have enough coins.")
            }

            return true
        }

        return false
    }
}
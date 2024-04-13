package io.github.chaosdave34.kitpvp.commands

import io.github.chaosdave34.kitpvp.KitPvp
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CustomItemCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.size > 2 || args.isEmpty()) return false

            var amount = 1
            if (args.size == 2) {
                amount = args[1].toIntOrNull() ?: 1
            }

            val customItem = KitPvp.INSTANCE.customItemHandler.customItems[args[0]] ?: return false
            val item = customItem.build(amount)
            sender.inventory.addItem(item)

            sender.sendMessage(Component.text("Gave you $amount ").append(customItem.getName()).append(Component.text(".")))
            return true
        }
        return false
    }
}
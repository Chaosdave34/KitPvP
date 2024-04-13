package io.github.chaosdave34.kitpvp.commands

import io.github.chaosdave34.kitpvp.KitPvp
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class LoopCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size < 3) return false

        val count = args[0].toIntOrNull()
        val period = args[1].toIntOrNull()
        if (count == null || period == null) return false

        if (count < 1 || period < 0) return false

        if (sender is Player) {
            object : BukkitRunnable() {
                var i = 0
                override fun run() {
                    val commandString = listOf(*args).subList(2, args.size).joinToString(" ")
                    if (i < count) sender.performCommand(commandString)
                    else {
                        sender.sendMessage(Component.text("Done!"))
                        this.cancel()
                    }
                    i++
                }
            }.runTaskTimer(KitPvp.INSTANCE, 0, period.toLong())
            return true
        }
        return false
    }
}
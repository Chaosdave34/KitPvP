package io.github.chaosdave34.kitpvp.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) return false;

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Component.text("No player was found!", NamedTextColor.RED));
            return true;
        }

        String targetMessage = "From " +  sender.getName() + ": " + String.join(" ", Arrays.copyOfRange(args, 1, args.length, String[].class));
        target.sendMessage(Component.text(targetMessage));

        String senderMessage = "To " +  target.getName() + ": " + String.join(" ", Arrays.copyOfRange(args, 1, args.length, String[].class));
        sender.sendMessage(senderMessage);

        return true;
    }
}

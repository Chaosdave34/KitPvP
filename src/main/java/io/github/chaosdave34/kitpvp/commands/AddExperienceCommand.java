package io.github.chaosdave34.kitpvp.commands;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AddExperienceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) return false;

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) return false;

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (amount < 1) return false;

        ExtendedPlayer.from(player).addExperiencePoints(amount);

        return true;
    }
}

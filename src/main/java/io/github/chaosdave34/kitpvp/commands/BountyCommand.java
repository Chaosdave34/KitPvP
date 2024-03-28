package io.github.chaosdave34.kitpvp.commands;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BountyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) return false;

        if (sender instanceof Player player) {
            // Get target player
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Component.text("The Player could not be found."));
                return true;
            }

            if (target.equals(player)) {
                player.sendMessage(Component.text("You can't place a bounty on yourself"));
                return true;
            }

            // Get bounty coin amount
            int amount = -1;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
            }

            if (amount <= 0) {
                player.sendMessage(Component.text("Invalid amount of coins."));
                return true;
            }

            // Check and purchase
            ExtendedPlayer extendedPlayer = ExtendedPlayer.from(player);
            if (extendedPlayer.purchase(amount)) {
                player.sendMessage("You placed a bounty of " + amount + " coins on " + target.getName() + ".");

                ExtendedPlayer.from(target).receivedBounty(amount);
            } else {
                player.sendMessage("You don't have enough coins.");
            }

            return true;
        }

        return false;
    }
}

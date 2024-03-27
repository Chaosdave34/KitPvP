package io.github.chaosdave34.kitpvp.commands;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GommeModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
            extendedPlayer.setGameState(ExtendedPlayer.GameState.DEBUG);
            p.setGameMode(GameMode.CREATIVE);
            return true;
        }

        return false;
    }
}

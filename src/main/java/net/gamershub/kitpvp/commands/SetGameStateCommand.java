package net.gamershub.kitpvp.commands;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SetGameStateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 1) {
                if (Arrays.stream(ExtendedPlayer.GameState.values()).map(ExtendedPlayer.GameState::name).toList().contains(args[0].toUpperCase())) {
                    ExtendedPlayer.GameState gameState = ExtendedPlayer.GameState.valueOf(args[0].toUpperCase());
                    KitPvpPlugin.INSTANCE.getExtendedPlayer(p).setGameState(gameState);
                    return true;
                }
            }
        }
        return false;
    }
}

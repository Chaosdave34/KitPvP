package net.gamershub.kitpvp.commands;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.kyori.adventure.text.Component;
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
                    ExtendedPlayer.from(p).setGameState(gameState);

                    p.sendMessage(Component.text("Set your game state to " + args[0] + "."));
                    return true;
                }
            }
        }
        return false;
    }
}

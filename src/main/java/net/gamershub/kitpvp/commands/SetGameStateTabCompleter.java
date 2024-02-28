package net.gamershub.kitpvp.commands;

import net.gamershub.kitpvp.ExtendedPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetGameStateTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], Arrays.stream(ExtendedPlayer.GameState.values()).map(ExtendedPlayer.GameState::name).toList(), completions);
            return completions;
        }
        return Collections.emptyList();
    }
}

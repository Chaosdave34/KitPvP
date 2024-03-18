package net.gamershub.kitpvp.textdisplays.impl;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.textdisplays.TextDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HighestLevelsTextDisplay extends TextDisplay {
    public HighestLevelsTextDisplay() {
        super("world", new Location(null, -3.5, 101.5, 10.5), 6);
    }

    @Override
    public @NotNull List<Component> getLines(Player p) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("Highest Levels:").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));

        Set<Map.Entry<UUID, Integer>> entrySet = KitPvpPlugin.INSTANCE.getHighestLevels().entrySet();

        int i = 1;
        for (Map.Entry<UUID, Integer> entry : entrySet.stream().sorted((v1, v2) -> Integer.compare(v2.getValue(), v1.getValue())).toList()) {
            lines.add(Component.literal(i + ". " + Bukkit.getOfflinePlayer(entry.getKey()).getName() + " - " + entry.getValue()));
            i++;
        }

        for (int j = i; j <= 5; j++) lines.add(Component.literal(j + ". ---"));

        return lines;
    }
}

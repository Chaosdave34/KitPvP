package io.github.chaosdave34.kitpvp.textdisplays;

import io.github.chaosdave34.ghlib.textdisplay.TextDisplay;
import io.github.chaosdave34.kitpvp.KitPvp;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HighestKillstreaksTextDisplay extends TextDisplay {
    public HighestKillstreaksTextDisplay() {
        super("world", new Location(null, 4.5,121.5,-8.5), 6);
    }

    @Override
    public @NotNull List<Component> getLines(Player p) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("Highest Killstreaks:").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));

        Set<Map.Entry<UUID, Integer>> entrySet = KitPvp.INSTANCE.getHighestKillstreaks().entrySet();

        int i = 1;
        for (Map.Entry<UUID, Integer> entry : entrySet.stream().sorted((v1, v2) -> Integer.compare(v2.getValue(), v1.getValue())).toList()) {
            lines.add(Component.literal(i + ". " + Bukkit.getOfflinePlayer(entry.getKey()).getName() + " - " + entry.getValue()));
            i++;
        }

        for (int j = i; j <= 5; j++) lines.add(Component.literal(j + ". ---"));

        return lines;
    }
}

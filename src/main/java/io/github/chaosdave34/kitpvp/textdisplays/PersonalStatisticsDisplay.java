package io.github.chaosdave34.kitpvp.textdisplays;

import io.github.chaosdave34.ghutils.textdisplay.TextDisplay;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PersonalStatisticsDisplay extends TextDisplay {
    private final ExtendedPlayer.GameType gameType;

    public PersonalStatisticsDisplay(ExtendedPlayer.GameType gameType, String world, Location position) {
        super(world, position, 5);
        this.gameType = gameType;
    }

    @Override
    public @NotNull List<Component> getLines(Player p) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);

        int highestKillStreak = extendedPlayer.getHighestKillStreak(gameType);
        int totalKills = extendedPlayer.getTotalKills(gameType);
        int totalDeaths = extendedPlayer.getTotalDeaths(gameType);

        double killDeathRation = totalDeaths == 0 ? totalKills : (double) totalKills / totalDeaths;

        return List.of(
                Component.literal("Personal Statistics:").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD),
                Component.literal("Highest Killstreak: " + highestKillStreak),
                Component.literal("Kills: " + totalKills),
                Component.literal("Deaths: " + totalDeaths),
                Component.literal((("K/D: " + (((double) Math.round(killDeathRation * 100)) / 100))))
        );

    }
}

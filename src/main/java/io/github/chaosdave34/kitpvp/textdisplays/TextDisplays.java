package io.github.chaosdave34.kitpvp.textdisplays;

import io.github.chaosdave34.ghutils.textdisplay.TextDisplay;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import org.bukkit.Location;
import org.bukkit.event.Listener;

public class TextDisplays implements Listener {
    public static TextDisplay JUMP;
    public static TextDisplay PERSONAL_STATISTICS_KITS;
    public static TextDisplay PERSONAL_STATISTICS_ELYTRA;
    public static TextDisplay INFO;
    public static TextDisplay HIGHEST_KILL_STREAKS_KITS;
    public static TextDisplay HIGHEST_KILL_STREAKS_ELYTRA;
    public static TextDisplay HIGHEST_LEVELS_KITS;
    public static TextDisplay AMUSEMENT_PARK_TEST_DISPLAY;

    public static void create() {
        JUMP = new JumpTextDisplay();

        PERSONAL_STATISTICS_KITS = new PersonalStatisticsDisplay(ExtendedPlayer.GameType.KITS, "world", new Location(null, 5.5, 121.5, 11.5));
        PERSONAL_STATISTICS_ELYTRA = new PersonalStatisticsDisplay(ExtendedPlayer.GameType.ELYTRA, "world_elytra", new Location(null, 10.5, 201.5, 4.5));

        INFO = new InfoTextDisplay();

        HIGHEST_KILL_STREAKS_KITS = new HighestKillstreaksTextDisplay(ExtendedPlayer.GameType.KITS, "world", new Location(null, 4.5, 121.5, -8.5));
        HIGHEST_KILL_STREAKS_ELYTRA = new HighestKillstreaksTextDisplay(ExtendedPlayer.GameType.ELYTRA, "world_elytra", new Location(null, 5.5, 201.5, 4.5));

        HIGHEST_LEVELS_KITS = new HighestLevelsTextDisplay();

        AMUSEMENT_PARK_TEST_DISPLAY = new AmusementParkTextDisplay();
    }
}

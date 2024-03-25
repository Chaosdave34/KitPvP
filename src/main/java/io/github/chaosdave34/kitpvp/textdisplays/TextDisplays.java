package io.github.chaosdave34.kitpvp.textdisplays;

import io.github.chaosdave34.ghlib.textdisplay.TextDisplay;
import org.bukkit.event.Listener;

public class TextDisplays implements Listener {
    public static TextDisplay JUMP;
    public static TextDisplay PERSONAL_STATISTICS;
    public static TextDisplay INFO;
    public static TextDisplay HIGHEST_KILLSTREAKS;
    public static TextDisplay HIGHEST_LEVELS;
    public static TextDisplay AMUSEMENT_PARK_TEST_DISPLAY;

    public static void create() {
        JUMP = new JumpTextDisplay();
        PERSONAL_STATISTICS = new PersonalStatisticsDisplay();
        INFO = new InfoTextDisplay();
        HIGHEST_KILLSTREAKS = new HighestKillstreaksTextDisplay();
        HIGHEST_LEVELS = new HighestLevelsTextDisplay();
        AMUSEMENT_PARK_TEST_DISPLAY = new AmusementParkTextDisplay();
    }
}

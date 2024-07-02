package io.github.chaosdave34.kitpvp.textdisplays;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import org.bukkit.Location;
import org.bukkit.event.Listener;

@Deprecated
public class TextDisplaysOld implements Listener {

    public static void create() {
        new PersonalStatisticsDisplay(ExtendedPlayer.GameType.KITS, "world", new Location(null, 5.5, 121.5, 11.5));
        new PersonalStatisticsDisplay(ExtendedPlayer.GameType.ELYTRA, "world_elytra", new Location(null, 10.5, 201.5, 4.5));

        new HighestKillstreaksTextDisplay(ExtendedPlayer.GameType.KITS, "world", new Location(null, 4.5, 121.5, -8.5));
        new HighestKillstreaksTextDisplay(ExtendedPlayer.GameType.ELYTRA, "world_elytra", new Location(null, 5.5, 201.5, 4.5));

        new HighestLevelsTextDisplay();
    }
}

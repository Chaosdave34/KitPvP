package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.kitpvp.kits.KitHandler;
import org.bukkit.Location;
import org.bukkit.event.Listener;

@Deprecated
public class FakePlayersOld implements Listener {
    public static void create() {
        new KitSelectorFakePlayer(KitHandler.KNIGHT, "world", new Location(null, -7.5, 120.0, 9.5, -90, 0));
        new KitSelectorFakePlayer(KitHandler.ZEUS, "world", new Location(null, -7.5, 120.0, 6.5, -90, 0));
        new KitSelectorFakePlayer(KitHandler.TANK, "world", new Location(null, -8.5, 120.0, 3.5, -90, 0));
        new KitSelectorFakePlayer(KitHandler.PROVOKER, "world", new Location(null, -8.5, 120.0, 0.5, -90, 0));
        new KitSelectorFakePlayer(KitHandler.ARCHER, "world", new Location(null, -7.5, 120.0, -2.5, -65, 0));
        new KitSelectorFakePlayer(KitHandler.ARTILLERYMAN, "world", new Location(null, -6.5, 120.0, -5.5, -45, 0));
        new KitSelectorFakePlayer(KitHandler.ASSASSIN, "world", new Location(null, -4.5, 120.0, -7.5, -15, 0));

        new KitSelectorFakePlayer(KitHandler.ENGINEER, "world", new Location(null, 11.5, 120.0, 9.5, 90, 0));
        new KitSelectorFakePlayer(KitHandler.MAGICIAN, "world", new Location(null, 11.5, 120.0, 6.5, 90, 0));
        new KitSelectorFakePlayer(KitHandler.VAMPIRE, "world", new Location(null, 12.5, 120.0, 3.5, 90, 0));
        new KitSelectorFakePlayer(KitHandler.CREEPER, "world", new Location(null, 12.5, 120.0, 0.5, 90, 0));
        new KitSelectorFakePlayer(KitHandler.ENDERMAN, "world", new Location(null, 11.5, 120.0, -2.5, 65, 0));
        new KitSelectorFakePlayer(KitHandler.POSEIDON, "world", new Location(null, 10.5, 120.0, -5.5, 45, 0));
        new KitSelectorFakePlayer(KitHandler.DEVIL, "world", new Location(null, 8.5, 120.0, -7.5, 15, 0));
    }
}

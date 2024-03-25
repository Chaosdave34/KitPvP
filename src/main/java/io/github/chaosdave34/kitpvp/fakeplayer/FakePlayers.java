package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghlib.fakeplayer.FakePlayer;
import io.github.chaosdave34.kitpvp.kits.KitHandler;
import org.bukkit.Location;
import org.bukkit.event.Listener;

public class FakePlayers implements Listener {
    public static FakePlayer CLASSIC_KIT;
    public static FakePlayer ZEUS_KIT;
    public static FakePlayer TANK_KIT;
    public static FakePlayer PROVOKER_KIT;
    public static FakePlayer ARCHER_KIT;
    public static FakePlayer ARTILLERYMAN_KIT;
    public static FakePlayer ASSASSIN_KIT;
    public static FakePlayer ENGINEER_KIT;
    public static FakePlayer MAGICIAN_KIT;
    public static FakePlayer VAMPIRE_KIT;
    public static FakePlayer CREEPER_KIT;
    public static FakePlayer ENDERMAN_KIT;
    public static FakePlayer POSEIDON_KIT;
    public static FakePlayer DEVIL_KIT;

    public static FakePlayer COSMETICS;

    public static void create() {
        CLASSIC_KIT = new KitSelectorFakePlayer(KitHandler.CLASSIC, "world", new Location(null, -7.5, 120.0, 9.5, -90, 0));
        ZEUS_KIT = new KitSelectorFakePlayer(KitHandler.ZEUS, "world", new Location(null, -7.5, 120.0, 6.5, -90, 0));
        TANK_KIT = new KitSelectorFakePlayer(KitHandler.TANK, "world", new Location(null, -8.5, 120.0, 3.5, -90, 0));
        PROVOKER_KIT = new KitSelectorFakePlayer(KitHandler.PROVOKER, "world", new Location(null, -8.5, 120.0, 0.5, -90, 0));
        ARCHER_KIT = new KitSelectorFakePlayer(KitHandler.ARCHER, "world", new Location(null, -7.5, 120.0, -2.5, -65, 0));
        ARTILLERYMAN_KIT = new KitSelectorFakePlayer(KitHandler.ARTILLERYMAN, "world", new Location(null, -6.5, 120.0, -5.5, -45, 0));
        ASSASSIN_KIT = new KitSelectorFakePlayer(KitHandler.ASSASSIN, "world", new Location(null, -4.5, 120.0, -7.5, -15, 0));

        ENGINEER_KIT = new KitSelectorFakePlayer(KitHandler.ENGINEER, "world", new Location(null, 11.5, 120.0, 9.5, 90, 0));
        MAGICIAN_KIT = new KitSelectorFakePlayer(KitHandler.MAGICIAN, "world", new Location(null, 11.5, 120.0, 6.5, 90, 0));
        VAMPIRE_KIT = new KitSelectorFakePlayer(KitHandler.VAMPIRE, "world", new Location(null, 12.5, 120.0, 3.5, 90, 0));
        CREEPER_KIT = new KitSelectorFakePlayer(KitHandler.CREEPER, "world", new Location(null, 12.5, 120.0, 0.5, 90, 0));
        ENDERMAN_KIT = new KitSelectorFakePlayer(KitHandler.ENDERMAN, "world", new Location(null, 11.5, 120.0, -2.5, 65, 0));
        POSEIDON_KIT = new KitSelectorFakePlayer(KitHandler.POSEIDON, "world", new Location(null, 10.5, 120.0, -5.5, 45, 0));
        DEVIL_KIT = new KitSelectorFakePlayer(KitHandler.DEVIL, "world", new Location(null, 8.5, 120.0, -7.5, 15, 0));

        COSMETICS = new CosmeticsFakePlayer();
    }
}

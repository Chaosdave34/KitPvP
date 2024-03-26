package io.github.chaosdave34.kitpvp.kits;

import io.github.chaosdave34.ghutils.Utils;
import io.github.chaosdave34.kitpvp.kits.impl.elytra.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ElytraKitHandler {
    private final Map<String, ElytraKit> kits = new HashMap<>();

    public static ElytraKit KNIGHT;
    public static ElytraKit SNIPER;
    public static ElytraKit PYRO;
    public static ElytraKit TANK;
    public static ElytraKit KNOCKER;
    public static ElytraKit ROCKET_LAUNCHER;
    public static ElytraKit POSEIDON;
    public static ElytraKit TELEPORTER;
    public static ElytraKit HEALER;
    public static ElytraKit CHEMIST;

    public ElytraKitHandler() {
        KNIGHT = registerKit(new KnightKit());
        SNIPER = registerKit(new SniperKit());
        PYRO = registerKit(new PyroKit());
        TANK = registerKit(new TankKit());
        KNOCKER = registerKit(new KnockerKit());
        ROCKET_LAUNCHER = registerKit(new RocketLauncherKit());
        POSEIDON = registerKit(new PoseidonKit());
        TELEPORTER = registerKit(new TeleporterKit());
        HEALER = registerKit(new HealerKit());
        CHEMIST = registerKit(new ChemistKit());
    }

    private ElytraKit registerKit(ElytraKit kit) {
        kits.put(kit.getId(), kit);
        Utils.registerEvents(kit);
        return kit;
    }
}

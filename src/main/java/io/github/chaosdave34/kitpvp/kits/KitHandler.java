package io.github.chaosdave34.kitpvp.kits;

import io.github.chaosdave34.ghutils.Utils;
import io.github.chaosdave34.kitpvp.kits.impl.kits.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class KitHandler {
    private final Map<String, Kit> kits = new HashMap<>();

    public static Kit CLASSIC;
    public static Kit ZEUS;
    public static Kit TANK;
    public static Kit PROVOKER;
    public static Kit ARCHER;
    public static Kit ARTILLERYMAN;
    public static Kit ASSASSIN;
    public static Kit ENGINEER;
    public static Kit MAGICIAN;
    public static Kit VAMPIRE;
    public static Kit CREEPER;
    public static Kit ENDERMAN;
    public static Kit POSEIDON;
    public static Kit DEVIL;

    public KitHandler() {
        CLASSIC = registerKit(new ClassicKit());
        ZEUS = registerKit(new ZeusKit());
        TANK = registerKit(new TankKit());
        PROVOKER = registerKit(new ProvokerKit());
        ARCHER = registerKit(new ArcherKit());
        ARTILLERYMAN = registerKit(new ArtilleryManKit());
        ASSASSIN = registerKit(new AssassinKit());
        ENGINEER = registerKit(new EngineerKit());
        MAGICIAN = registerKit(new MagicianKit());
        VAMPIRE = registerKit(new VampireKit());
        CREEPER = registerKit(new CreeperKit());
        ENDERMAN = registerKit(new EndermanKit());
        POSEIDON = registerKit(new PoseidonKit());
        DEVIL = registerKit(new DevilKit());
    }

    private Kit registerKit(Kit kit) {
        kits.put(kit.getId(), kit);
        Utils.registerEvents(kit);
        return kit;
    }
}

package net.gamershub.kitpvp.kits;

import lombok.Getter;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.kits.impl.*;

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
        CLASSIC = createKit(new ClassicKit());
        ZEUS = createKit(new ZeusKit());
        TANK = createKit(new TankKit());
        PROVOKER = createKit(new ProvokerKit());
        ARCHER = createKit(new ArcherKit());
        ARTILLERYMAN = createKit(new ArtilleryManKit());
        ASSASSIN = createKit(new AssassinKit());
        ENGINEER = createKit(new EngineerKit());
        MAGICIAN = createKit(new MagicianKit());
        VAMPIRE = createKit(new VampireKit());
        CREEPER = createKit(new CreeperKit());
        ENDERMAN = createKit(new EndermanKit());
        POSEIDON = createKit(new PoseidonKit());
        DEVIL = createKit(new DevilKit());
    }

    private Kit createKit(Kit kit) {
        kits.put(kit.getId(), kit);
        Utils.registerEvents(kit);
        return kit;
    }
}

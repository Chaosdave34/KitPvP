package net.gamershub.kitpvp.kits;

import lombok.Getter;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.kits.impl.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

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
    public static Kit CROSSBOW;
    public static Kit RUNNER;
    public static Kit TRAPPER;
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
        CROSSBOW = createKit(new CrossbowKit());
        RUNNER = createKit(new RunnerKit());
        TRAPPER = createKit(new TrapperKit());
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

    public void clearKit(Player p) {
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        extendedPlayer.setSelectedKitId(null);

        AttributeInstance maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) maxHealth.setBaseValue(20);

        AttributeInstance movementSpeed = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (movementSpeed != null) movementSpeed.setBaseValue(0.1);

        p.getInventory().clear();
    }
}

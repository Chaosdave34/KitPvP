package net.gamershub.kitpvp.abilities.impl.vampire;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BatMorhpAbility extends Ability {
    public BatMorhpAbility() {
        super("bat_morph", "Bat Morph", AbilityType.RIGHT_CLICK, 30);
    }


    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Morph into a bat.");
    }

    @Override
    public boolean onAbility(Player p) {
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        extendedPlayer.morph(EntityType.BAT);

        Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, () -> {
            Entity vehicle = p.getVehicle();
            if (vehicle != null) vehicle.removePassenger(p);
        }, 10 * 20);

        return true;
    }
}

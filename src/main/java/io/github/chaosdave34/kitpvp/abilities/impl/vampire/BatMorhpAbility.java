package io.github.chaosdave34.kitpvp.abilities.impl.vampire;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityType;
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
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        extendedPlayer.morph(EntityType.BAT);

        Bukkit.getScheduler().runTaskLater(KitPvp.INSTANCE, () -> {
            Entity vehicle = p.getVehicle();
            if (vehicle != null) vehicle.removePassenger(p);
        }, 10 * 20);

        return true;
    }
}

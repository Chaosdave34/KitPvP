package io.github.chaosdave34.kitpvp.abilities.impl.vampire;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BatMorhpAbility extends Ability {
    public BatMorhpAbility() {
        super("bat_morph", "Bat Morph", Type.RIGHT_CLICK, 30);
    }


    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Morph into a bat.");
    }

    @Override
    public boolean onAbility(Player p) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        extendedPlayer.morph(EntityType.BAT);

        new AbilityRunnable(extendedPlayer) {
            @Override
            public void runInGame() {
                Entity vehicle = p.getVehicle();
                if (vehicle != null) vehicle.removePassenger(p);
            }
        }.runTaskLater(KitPvp.INSTANCE, 10 * 20);

        return true;
    }
}

package net.gamershub.kitpvp.abilities.impl.engineer;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.gamershub.kitpvp.entities.CustomEntityHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TurretAbility extends Ability {
    public TurretAbility() {
        super("turret", "Turret", AbilityType.RIGHT_CLICK, 120);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Deploy a mobile turret.");
    }

    @Override
    public boolean onAbility(Player p) {
        CustomEntityHandler.TURRET.spawn(p, p.getLocation());
        return true;
    }
}

package io.github.chaosdave34.kitpvp.abilities.impl.engineer;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityType;
import io.github.chaosdave34.kitpvp.entities.CustomEntities;
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
        CustomEntities.TURRET.spawn(p, p.getLocation());
        return true;
    }
}

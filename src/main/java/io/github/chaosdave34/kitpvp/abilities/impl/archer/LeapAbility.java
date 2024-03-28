package io.github.chaosdave34.kitpvp.abilities.impl.archer;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LeapAbility extends Ability {
    public LeapAbility() {
        super("leap", "Leap", AbilityType.RIGHT_CLICK, 20);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Leap into the direction",
                "you are looking at."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        Vector launchVector = p.getEyeLocation().getDirection().normalize();
        launchVector.multiply(2);
        p.setVelocity(launchVector);
        return true;
    }
}

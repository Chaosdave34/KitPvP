package net.gamershub.kitpvp.abilities.impl.poseidon;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StormAbility extends Ability {
    public StormAbility() {
        super("storm", "Storm", AbilityType.SNEAK_RIGHT_CLICK, 120);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Gather a storm for 30 seconds.");
    }

    @Override
    public boolean onAbility(Player p) {
        p.getWorld().setStorm(true);
        p.getWorld().setThundering(true);
        p.getWorld().setThunderDuration(600);
        return true;
    }
}

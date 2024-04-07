package io.github.chaosdave34.kitpvp.abilities.impl.poseidon;

import io.github.chaosdave34.kitpvp.abilities.Ability;

import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StormAbility extends Ability {
    public StormAbility() {
        super("storm", "Storm", Type.SNEAK_RIGHT_CLICK, 120);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Gather a storm for 30 seconds.");
    }

    @Override
    public boolean onAbility(Player p) {
        World world = p.getWorld();
        world.setStorm(true);
        world.setWeatherDuration(600);
        world.setThundering(true);
        world.setThunderDuration(600);
        return true;
    }
}

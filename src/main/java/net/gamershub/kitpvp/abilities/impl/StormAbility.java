package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StormAbility extends Ability {
    public StormAbility() {
        super("storm", "Storm", AbilityType.RIGHT_CLICK, 120);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return List.of(Component.text("Gather a storm for 30 seconds.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public boolean onAbility(Player p) {
        p.getWorld().setStorm(true);
        p.getWorld().setThundering(true);
        p.getWorld().setThunderDuration(600);
        return true;
    }
}

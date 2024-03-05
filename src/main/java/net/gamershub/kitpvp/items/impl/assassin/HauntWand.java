package net.gamershub.kitpvp.items.impl.assassin;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HauntWand extends CustomItem {
    public HauntWand() {
        super(Material.FEATHER, "haunt_wand", false);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Haunt Wand");
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Happy haunting!");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.HAUNT);
    }
}

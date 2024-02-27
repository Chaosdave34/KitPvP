package net.gamershub.kitpvp.items.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HauntWand extends CustomItem {
    public HauntWand() {
        super(Material.FEATHER, "haunt_wand", false);
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Haunt Wand").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return List.of(Component.text("Happy haunting!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.HAUNT);
    }
}
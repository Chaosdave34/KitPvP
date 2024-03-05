package net.gamershub.kitpvp.items.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TankBoots extends CustomItem {
    public TankBoots() {
        super(Material.DIAMOND_BOOTS, "tank_boots", false);
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Tank Boots").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.SEISMIC_WAVE);
    }
}

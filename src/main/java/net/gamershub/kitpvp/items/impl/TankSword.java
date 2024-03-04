package net.gamershub.kitpvp.items.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TankSword extends CustomItem {
    public TankSword() {
        super(Material.IRON_AXE, "tank_sword", false);
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Tank Sword").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.SEISMIC_WAVE);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        setCustomModelData(itemStack, 1);
    }
}

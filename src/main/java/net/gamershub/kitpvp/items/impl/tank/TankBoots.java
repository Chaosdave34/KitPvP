package net.gamershub.kitpvp.items.impl.tank;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TankBoots extends CustomItem {
    public TankBoots() {
        super(Material.DIAMOND_BOOTS, "tank_boots");
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Tank Boots");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.SEISMIC_WAVE);
    }
}

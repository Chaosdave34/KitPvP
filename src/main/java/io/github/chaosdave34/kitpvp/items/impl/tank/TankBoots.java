package io.github.chaosdave34.kitpvp.items.impl.tank;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
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

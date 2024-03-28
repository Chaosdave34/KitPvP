package io.github.chaosdave34.kitpvp.items.impl.archer;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Leap extends CustomItem {
    public Leap() {
        super(Material.FEATHER, "leap");
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Leap");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.LEAP);
    }
}

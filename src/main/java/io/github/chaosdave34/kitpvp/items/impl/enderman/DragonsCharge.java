package io.github.chaosdave34.kitpvp.items.impl.enderman;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DragonsCharge extends CustomItem {
    public DragonsCharge() {
        super(Material.FIRE_CHARGE, "dragons_charge", false, true);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Dragon's Charge");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.DRAGON_FIREBALL);
    }
}

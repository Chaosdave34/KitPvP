package io.github.chaosdave34.kitpvp.items.impl.engineer;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TurretItem extends CustomItem {

    public TurretItem() {
        super(Material.STRAY_SPAWN_EGG, "turret", false, true);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Turret");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.TURRET);
    }
}

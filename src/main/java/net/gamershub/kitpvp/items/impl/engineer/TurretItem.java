package net.gamershub.kitpvp.items.impl.engineer;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
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

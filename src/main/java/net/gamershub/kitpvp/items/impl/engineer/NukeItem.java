package net.gamershub.kitpvp.items.impl.engineer;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NukeItem extends CustomItem {
    public NukeItem() {
        super(Material.FIREWORK_ROCKET, "nuke", false, true);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Nuke");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.NUKE);
    }
}

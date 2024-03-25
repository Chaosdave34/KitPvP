package io.github.chaosdave34.kitpvp.items.impl.provoker;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
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

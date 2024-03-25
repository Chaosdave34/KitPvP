package io.github.chaosdave34.kitpvp.items.impl.zeus;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightningWand extends CustomItem {


    public LightningWand() {
        super(Material.STICK, "lightning_wand", false);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Lightning Wand");
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Stolen from Zeus.");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.LIGHTNING, AbilityHandler.THUNDERSTORM);
    }
}


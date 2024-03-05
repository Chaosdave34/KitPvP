package net.gamershub.kitpvp.items.impl.zeus;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
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


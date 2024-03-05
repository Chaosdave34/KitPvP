package net.gamershub.kitpvp.items.impl.creeper;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FireballWand extends CustomItem {
    public FireballWand() {
        super(Material.FIRE_CHARGE, "fireball_wand", false, true);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Fireball Wand");
    }

    @Override
    public @NotNull List<Component> getDescription() {
        List<Component> description = new ArrayList<>();
        description.add(Component.text("Handle with care... ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        description.add(Component.text("or not, if you're feeling hot-headed!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        return description;
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.FIREBALL);
    }
}


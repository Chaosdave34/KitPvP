package net.gamershub.kitpvp.items.impl.trapper;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrapWand extends CustomItem {
    public TrapWand() {
        super(Material.STICK, "trap_wand", false);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Trap Wand");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.ANVIL, AbilityHandler.TRAP);
    }
}
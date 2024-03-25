package io.github.chaosdave34.kitpvp.items.impl.engineer;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
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

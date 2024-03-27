package io.github.chaosdave34.kitpvp.items.impl.provoker;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpookSword extends CustomItem {
    public SpookSword(){
        super(Material.DIAMOND_SWORD, "spook_sword");
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Spook Sword");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.SPOOK);
    }
}

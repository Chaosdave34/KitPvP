package io.github.chaosdave34.kitpvp.items.impl.tank;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TankAxe extends CustomItem {
    public TankAxe() {
        super(Material.IRON_AXE, "tank_axe");
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Tank Axe");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.FORTIFY);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        setCustomModelData(itemStack, 1);
    }
}

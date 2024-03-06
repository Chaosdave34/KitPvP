package net.gamershub.kitpvp.items.impl.tank;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TankAxe extends CustomItem {
    public TankAxe() {
        super(Material.IRON_AXE, "tank_axe", false);
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

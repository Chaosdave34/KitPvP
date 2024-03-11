package net.gamershub.kitpvp.items.impl.creeper;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FireballSword extends CustomItem {
    public FireballSword() {
        super(Material.IRON_SWORD, "fireball_sword", false, true);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Fireball Sword");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.FIREBALL);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        setCustomModelData(itemStack, 2);
    }
}


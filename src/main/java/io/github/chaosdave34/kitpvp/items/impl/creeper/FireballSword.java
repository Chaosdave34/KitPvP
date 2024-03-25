package io.github.chaosdave34.kitpvp.items.impl.creeper;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FireballSword extends CustomItem {
    public FireballSword() {
        super(Material.IRON_SWORD, "fireball_sword");
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


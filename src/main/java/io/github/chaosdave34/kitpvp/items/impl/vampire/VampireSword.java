package io.github.chaosdave34.kitpvp.items.impl.vampire;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.enchantments.CustomEnchantments;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VampireSword extends CustomItem {
    public VampireSword() {
        super(Material.DIAMOND_SWORD, "vampire_sword");
    }


    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Vampire Sword");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return  List.of(AbilityHandler.BAT_MORPH);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addEnchantment(CustomEnchantments.LIFE_STEAL, 4);
        setCustomModelData(itemStack, 1);
    }
}

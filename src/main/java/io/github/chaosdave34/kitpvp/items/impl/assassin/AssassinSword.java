package io.github.chaosdave34.kitpvp.items.impl.assassin;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.enchantments.CustomEnchantments;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AssassinSword extends CustomItem {
    public AssassinSword() {
        super(Material.IRON_SWORD, "assasin_sword");
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Assassin Sword");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.HAUNT);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addEnchantment(CustomEnchantments.BACKSTAB, 1);
        setCustomModelData(itemStack, 3);
    }
}

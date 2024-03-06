package net.gamershub.kitpvp.items.impl.vampire;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.enchantments.CustomEnchantmentHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VampireSword extends CustomItem {
    public VampireSword() {
        super(Material.DIAMOND_SWORD, "vampire_sword", false);
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
        itemStack.addEnchantment(CustomEnchantmentHandler.LIFE_STEAL, 4);
        setCustomModelData(itemStack, 1);
    }
}

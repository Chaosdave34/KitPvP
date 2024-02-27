package net.gamershub.kitpvp.items.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.enchantments.EnchantmentHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
        return Component.text("Vampire Sword", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return List.of(Component.text("Batman?", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return  List.of(AbilityHandler.BAT_MORPH);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addEnchantment(EnchantmentHandler.LIFE_STEAL, 4);
    }
}
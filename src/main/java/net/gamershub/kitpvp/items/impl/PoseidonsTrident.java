package net.gamershub.kitpvp.items.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PoseidonsTrident extends CustomItem {
    public PoseidonsTrident() {
        super(Material.TRIDENT, "poseidons_trident", false);
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Poseidon's Trident").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.STORM);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addUnsafeEnchantment(Enchantment.LOYALTY, 10);
        itemStack.addUnsafeEnchantment(Enchantment.CHANNELING, 5);
    }
}

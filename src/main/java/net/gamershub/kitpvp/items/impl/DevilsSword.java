package net.gamershub.kitpvp.items.impl;

import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DevilsSword extends CustomItem {
    public DevilsSword(){
        super(Material.IRON_SWORD, "devils_sword", false);
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Devil's Sword").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
    }
}

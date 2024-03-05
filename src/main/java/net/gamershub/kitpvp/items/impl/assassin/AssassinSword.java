package net.gamershub.kitpvp.items.impl.assassin;

import net.gamershub.kitpvp.enchantments.CustomEnchantmentHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AssassinSword extends CustomItem {
    public AssassinSword() {
        super(Material.IRON_SWORD, "assasin_sword", false);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Assassin Sword");
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addEnchantment(CustomEnchantmentHandler.BACKSTAB, 1);
    }
}

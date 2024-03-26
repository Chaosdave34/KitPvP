package io.github.chaosdave34.kitpvp.items.impl.artilleryman;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RocketLauncher extends CustomItem {
    public RocketLauncher() {
        super(Material.CROSSBOW, "rocket_launcher");
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Rocket Launcher");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.ENHANCE);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addEnchantment(Enchantment.MULTISHOT, 1);
        itemStack.addEnchantment(Enchantment.QUICK_CHARGE, 3);

        itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);

        setCustomModelData(itemStack, 1);
    }
}

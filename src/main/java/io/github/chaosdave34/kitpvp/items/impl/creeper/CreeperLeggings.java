package io.github.chaosdave34.kitpvp.items.impl.creeper;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreeperLeggings extends CustomItem {
    public CreeperLeggings() {
        super(Material.LEATHER_LEGGINGS, "creeper_leggings");
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Creeper Leggings");
    }


    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.EXPLODE);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);

        setLeatherArmorColor(itemStack, Color.LIME);
    }
}

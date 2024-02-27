package net.gamershub.kitpvp.items.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreeperLeggings extends CustomItem {
    public CreeperLeggings() {
        super(Material.LEATHER_LEGGINGS, "Creeper Leggings", false);
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Creeper Leggings").decoration(TextDecoration.ITALIC, false);
    }


    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.EXPLODE);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);

        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        leatherArmorMeta.setColor(Color.LIME);
        itemStack.setItemMeta(leatherArmorMeta);
    }
}

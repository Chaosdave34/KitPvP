package io.github.chaosdave34.kitpvp.kits.impl.kitpvp;

import io.github.chaosdave34.kitpvp.companions.Companion;
import io.github.chaosdave34.kitpvp.companions.CompanionHandler;
import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.Kit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class DevilKit extends Kit { // Add special ability
    public DevilKit() {
        super("devil", "Devil");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        leatherHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
        setLeatherArmorColor(leatherHelmet, Color.RED);
        return leatherHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        leatherChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
        setLeatherArmorColor(leatherChestplate, Color.RED);
        return leatherChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
        setLeatherArmorColor(leatherLeggings, Color.RED);
        return leatherLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherBoots.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 4);
        setLeatherArmorColor(leatherBoots, Color.RED);
        return leatherBoots;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
        setCustomModelData(bow, 1);

        return new ItemStack[]{
                CustomItemHandler.DEVILS_SWORD.build(),
                bow,
                new ItemStack(Material.ARROW),
        };
    }

    @Override
    public ItemStack[] getKillRewards() {
        return new ItemStack[]{
                new ItemStack(Material.GOLDEN_APPLE),
                new ItemStack(Material.NETHERRACK, 32),
                new ItemStack(Material.TWISTING_VINES, 2)
        };
    }

    @Override
    public Companion getCompanion() {
        return CompanionHandler.ZOMBIFIED_PIGLIN;
    }
}

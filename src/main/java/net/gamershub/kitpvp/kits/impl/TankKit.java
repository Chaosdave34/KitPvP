package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TankKit extends Kit {
    public TankKit() {
        super("tank", "Tank");
    }

    @Override
    public double getMaxHealth() {
        return 40;
    }

    @Override
    public double getMovementSpeed() {
        return 0.05;
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET);
        diamondHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        return diamondHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        diamondChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        return diamondChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        diamondLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        return diamondLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
        diamondBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        return diamondBoots;
    }

    @Override
    public ItemStack getOffhandContent() {
        return new ItemStack(Material.SHIELD);
    }

    @Override
    public ItemStack[] getInventoryContent() {
        return new ItemStack[]{
                CustomItemHandler.TANK_SWORD.build(1),
                new ItemStack(Material.WATER_BUCKET),
        };
    }

    @Override
    public ItemStack[] getKillRewards() {
        return new ItemStack[]{
                new ItemStack(Material.GOLDEN_APPLE, 2),
                new ItemStack(Material.COBBLED_DEEPSLATE, 32),
        };
    }
}

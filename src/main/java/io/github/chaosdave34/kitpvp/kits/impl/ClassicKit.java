package io.github.chaosdave34.kitpvp.kits.impl;

import io.github.chaosdave34.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ClassicKit extends Kit {
    public ClassicKit() {
        super("classic", "Classic");
    }

    @Override
    public ItemStack getHeadContent() {
        return new ItemStack(Material.IRON_HELMET);
    }

    @Override
    public ItemStack getChestContent() {
        return new ItemStack(Material.IRON_CHESTPLATE);
    }

    @Override
    public ItemStack getLegsContent() {
        return new ItemStack(Material.IRON_LEGGINGS);
    }

    @Override
    public ItemStack getFeetContent() {
        return new ItemStack(Material.IRON_BOOTS);
    }

    @Override
    public ItemStack getOffhandContent() {
        return new ItemStack(Material.SHIELD);
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        return new ItemStack[]{
                new ItemStack(Material.DIAMOND_SWORD),
                new ItemStack(Material.IRON_AXE),
                bow,
                new ItemStack(Material.ARROW),
                new ItemStack(Material.WATER_BUCKET),
        };
    }

    @Override
    public ItemStack[] getKillRewards() {
        return new ItemStack[]{
                new ItemStack(Material.GOLDEN_APPLE),
                new ItemStack(Material.COBBLESTONE, 32),
        };
    }
}

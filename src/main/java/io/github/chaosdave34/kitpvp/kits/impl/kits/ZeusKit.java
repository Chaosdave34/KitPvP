package io.github.chaosdave34.kitpvp.kits.impl.kits;

import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.Kit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ZeusKit extends Kit {
    public ZeusKit() {
        super("zeus", "Zeus");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack goldenHelmet = new ItemStack(Material.GOLDEN_HELMET);
        goldenHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        return goldenHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack goldenChestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
        goldenChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        return goldenChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack goldenLeggings = new ItemStack(Material.GOLDEN_LEGGINGS);
        goldenLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(goldenLeggings, Color.YELLOW);
        return goldenLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack goldenBoots = new ItemStack(Material.GOLDEN_BOOTS);
        goldenBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        return goldenBoots;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack goldenSword = new ItemStack(Material.GOLDEN_SWORD);
        goldenSword.addEnchantment(Enchantment.DAMAGE_ALL, 2);

        return new ItemStack[]{
                goldenSword,
                CustomItemHandler.LIGHTNING_WAND.build(),
                new ItemStack(Material.WATER_BUCKET),
        };
    }

    @Override
    public ItemStack[] getKillRewards() {
        return new ItemStack[]{
                new ItemStack(Material.GOLDEN_APPLE),
                new ItemStack(Material.GOLD_BLOCK, 32),
        };
    }
}

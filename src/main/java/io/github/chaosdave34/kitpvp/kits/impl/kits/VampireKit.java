package io.github.chaosdave34.kitpvp.kits.impl.kits;

import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.Kit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class VampireKit extends Kit {
    public VampireKit() {
        super("vampire", "Vampire");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(helmet, Color.RED);
        return helmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(chestplate, Color.BLACK);
        return chestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(leggings, Color.BLACK);
        return leggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(boots, Color.BLACK);
        return boots;
    }

    @Override
    public ItemStack @NotNull [] getInventoryContent() {
        return new ItemStack[]{
                CustomItemHandler.VAMPIRE_SWORD.build(),
        };
    }

    @Override
    public ItemStack @NotNull [] getKillRewards() {
        return new ItemStack[]{
                new ItemStack(Material.RED_NETHER_BRICKS, 32),
        };
    }
}

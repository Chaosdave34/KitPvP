package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ProvokerKit extends Kit {
    public ProvokerKit() {
        super("provoker", "Provoker (WIP)");
    }

    @Override
    public double getMovementSpeed() {
        return 0.3;
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack diamondHelmet = new ItemStack(Material.LEATHER_HELMET);
        diamondHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        return diamondHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack diamondChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        diamondChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        return diamondChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack diamondLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        diamondLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        return diamondLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack diamondBoots = new ItemStack(Material.LEATHER_BOOTS);
        diamondBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        return diamondBoots;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
        diamondSword.addEnchantment(Enchantment.DAMAGE_ALL, 5);

        ItemStack knockBackStick = new ItemStack(Material.STICK);
        knockBackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        return new ItemStack[]{ // Todo: Rocket Item
                diamondSword,
                knockBackStick,
                new ItemStack(Material.COBBLESTONE, 64),
                new ItemStack(Material.WATER_BUCKET),
        };
    }


}
package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EngineerKit extends Kit {
    public EngineerKit() {
        super("engineer", "Engineer");
    }

    @Override
    public double getMovementSpeed() {
        return 0.2;
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
        ItemStack knockBackStick = new ItemStack(Material.STICK);
        knockBackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

        return new ItemStack[]{
                CustomItemHandler.SPOOK_SWORD.build(),
                knockBackStick,
                CustomItemHandler.NUKE.build(),
                CustomItemHandler.TURRET.build(),
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

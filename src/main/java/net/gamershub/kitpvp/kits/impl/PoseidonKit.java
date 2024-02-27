package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class PoseidonKit extends Kit {
    public PoseidonKit() {
        super("poseidon", "Poseidon");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack turtleHelmet = new ItemStack(Material.TURTLE_HELMET);
        turtleHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        turtleHelmet.addEnchantment(Enchantment.OXYGEN, 3);
        turtleHelmet.addEnchantment(Enchantment.WATER_WORKER, 1);
        return turtleHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        leatherChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(leatherChestplate, Color.AQUA);
        return leatherChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(leatherLeggings, Color.AQUA);
        return leatherLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherBoots.addEnchantment(Enchantment.DEPTH_STRIDER, 3);
        setLeatherArmorColor(leatherBoots, Color.AQUA);
        return leatherBoots;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack riptideTrident = new ItemStack(Material.TRIDENT);
        riptideTrident.addUnsafeEnchantment(Enchantment.RIPTIDE, 5);

        return new ItemStack[]{
                CustomItemHandler.POSEIDONS_TRIDENT.build(1),
                riptideTrident,
                new ItemStack(Material.GOLDEN_APPLE, 3),
                new ItemStack(Material.PRISMARINE, 64),
                new ItemStack(Material.WATER_BUCKET),
        };
    }
}

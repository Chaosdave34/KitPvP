package net.gamershub.kitpvp.kits.impl;

import com.mojang.datafixers.util.Pair;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class ArcherKit extends Kit {
    public ArcherKit() {
        super("archer", "Archer");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        setLeatherArmorColor(leatherHelmet, Color.GREEN);
        return leatherHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        setLeatherArmorColor(leatherChestplate, Color.GREEN);
        return leatherChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        return new ItemStack(Material.CHAINMAIL_LEGGINGS);
    }

    @Override
    public ItemStack getFeetContent() {
        return new ItemStack(Material.CHAINMAIL_BOOTS);
    }


    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);

        return new ItemStack[]{
                new ItemStack(Material.STONE_SWORD),
                bow,
                new ItemStack(Material.ARROW),
                new ItemStack(Material.GOLDEN_APPLE, 3),
                new ItemStack(Material.COBBLESTONE, 64),
                new ItemStack(Material.COBBLESTONE, 64),
                new ItemStack(Material.WATER_BUCKET),
        };
    }

    @Override
    public List<Pair<PotionEffectType, Integer>> getPotionEffects() {
        return List.of(Pair.of(PotionEffectType.JUMP, 2));
    }
}

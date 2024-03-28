package io.github.chaosdave34.kitpvp.kits.impl.kits;

import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.Kit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EndermanKit extends Kit {
    public EndermanKit() {
        super("enderman", "Enderman");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        leatherHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
        setLeatherArmorColor(leatherHelmet, Color.BLACK);
        return leatherHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        leatherChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherChestplate.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
        setLeatherArmorColor(leatherChestplate, Color.BLACK);
        return leatherChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
        setLeatherArmorColor(leatherLeggings, Color.BLACK);
        return leatherLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherBoots.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
        setLeatherArmorColor(leatherBoots, Color.BLACK);
        return leatherBoots;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        return new ItemStack[]{
                CustomItemHandler.ENDER_SWORD.build(),
        };
    }

    @Override
    public ItemStack[] getKillRewards() {
        return new ItemStack[]{
                new ItemStack(Material.GOLDEN_APPLE),
                new ItemStack(Material.END_STONE, 32),
        };
    }
}

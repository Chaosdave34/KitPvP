package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

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
        ItemMeta diamondHelmetMeta =  diamondHelmet.getItemMeta();
        diamondHelmetMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        diamondHelmet.setItemMeta(diamondHelmetMeta);
        return diamondHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        diamondChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta diamondChestplateMeta =  diamondChestplate.getItemMeta();
        diamondChestplateMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        diamondChestplate.setItemMeta(diamondChestplateMeta);
        return diamondChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        diamondLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta diamondLeggingsMeta =  diamondLeggings.getItemMeta();
        diamondLeggingsMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        diamondLeggings.setItemMeta(diamondLeggingsMeta);
        return diamondLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
        diamondBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta diamondBootsMeta =  diamondBoots.getItemMeta();
        diamondBootsMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        diamondBoots.setItemMeta(diamondBootsMeta);
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

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
    public ItemStack getHeadContent() {
        return  new ItemStack(Material.DIAMOND_HELMET);
    }

    @Override
    public ItemStack getChestContent() {
        return new ItemStack(Material.DIAMOND_CHESTPLATE);
    }

    @Override
    public ItemStack getLegsContent() {
        return new ItemStack(Material.DIAMOND_LEGGINGS);
    }

    @Override
    public ItemStack getFeetContent() {
        return CustomItemHandler.TANK_BOOTS.build();
    }

    public ItemStack getFortifiedHeadContent() {
        ItemStack netheriteHelmet = new ItemStack(Material.NETHERITE_HELMET);
        netheriteHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta netheriteHelmetMeta =  netheriteHelmet.getItemMeta();
        netheriteHelmetMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD));
        netheriteHelmet.setItemMeta(netheriteHelmetMeta);
        return netheriteHelmet;
    }

    public ItemStack getFortifiedChestContent() {
        ItemStack netheriteChestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        netheriteChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta netheriteChestplateMeta =  netheriteChestplate.getItemMeta();
        netheriteChestplateMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        netheriteChestplate.setItemMeta(netheriteChestplateMeta);
        return netheriteChestplate;
    }

    public ItemStack getFortifiedLegsContent() {
        ItemStack netheriteLeggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        netheriteLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta netheriteLeggingsMeta =  netheriteLeggings.getItemMeta();
        netheriteLeggingsMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS));
        netheriteLeggings.setItemMeta(netheriteLeggingsMeta);
        return netheriteLeggings;
    }

    public ItemStack getFortifiedFeetContent() {
        ItemStack netheriteBoots = new ItemStack(Material.NETHERITE_BOOTS);
        netheriteBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemMeta netheriteBootsMeta =  netheriteBoots.getItemMeta();
        netheriteBootsMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET));
        netheriteBoots.setItemMeta(netheriteBootsMeta);
        return netheriteBoots;
    }

    @Override
    public ItemStack getOffhandContent() {
        return new ItemStack(Material.SHIELD);
    }

    @Override
    public ItemStack[] getInventoryContent() {
        return new ItemStack[]{
                CustomItemHandler.TANK_AXE.build(),
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

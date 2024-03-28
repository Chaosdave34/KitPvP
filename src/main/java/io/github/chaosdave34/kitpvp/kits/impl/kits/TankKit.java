package io.github.chaosdave34.kitpvp.kits.impl.kits;

import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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
        netheriteHelmet.editMeta(itemMeta -> itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD)));
        return netheriteHelmet;
    }

    public ItemStack getFortifiedChestContent() {
        ItemStack netheriteChestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        netheriteChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        netheriteChestplate.editMeta(itemMeta -> itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST)));
        return netheriteChestplate;
    }

    public ItemStack getFortifiedLegsContent() {
        ItemStack netheriteLeggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        netheriteLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        netheriteLeggings.editMeta(itemMeta -> itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS)));

        return netheriteLeggings;
    }

    public ItemStack getFortifiedFeetContent() {
        ItemStack netheriteBoots = new ItemStack(Material.NETHERITE_BOOTS);
        netheriteBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        netheriteBoots.editMeta(itemMeta -> itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET)));
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

package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class CrossbowKit extends Kit {
    public CrossbowKit() {
        super("crossbow", "Crossbow");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        leatherHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
        setLeatherArmorColor(leatherHelmet, Color.NAVY);
        return leatherHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        return CustomItemHandler.JETPACK.build();
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
        setLeatherArmorColor(leatherLeggings, Color.NAVY);
        return leatherLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherBoots.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
        setLeatherArmorColor(leatherBoots, Color.NAVY);
        return leatherBoots;
    }

    @Override
    public ItemStack getOffhandContent() {
        ItemStack rocket = new ItemStack(Material.FIREWORK_ROCKET, 64);
        FireworkMeta rocketMeta = (FireworkMeta) rocket.getItemMeta();
        rocketMeta.setPower(5);
        rocketMeta.addEffect(FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.BALL).build());
        rocket.setItemMeta(rocketMeta);
        return rocket;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        setCustomModelData(crossbow, 1);

        return new ItemStack[]{
                crossbow,
                new ItemStack(Material.STONE_SWORD),
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

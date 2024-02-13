package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TestKit extends Kit {
    public TestKit() {
        super("test_kit","Test Kit");
    }

    @Override
    protected double getMaxHealth() {
        return 40;
    }

    @Override
    protected double getMovementSpeed() {
        return 0.3;
    }

    @Override
    public ItemStack[] getArmorContents() {
        return new ItemStack[]{new ItemStack(Material.IRON_BOOTS),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.IRON_HELMET)};
    }
}

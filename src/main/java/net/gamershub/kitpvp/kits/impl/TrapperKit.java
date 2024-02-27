package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TrapperKit extends Kit {
    public TrapperKit() {
        super("trapper", "Trapper");
    }

    @Override
    public ItemStack getHeadContent() {
        return new ItemStack(Material.CHAINMAIL_HELMET);
    }

    @Override
    public ItemStack getChestContent() {
        return new ItemStack(Material.CHAINMAIL_CHESTPLATE);
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
        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
        stoneSword.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
        stoneSword.addEnchantment(Enchantment.DAMAGE_ALL, 2);


        return new ItemStack[]{
                stoneSword,
                CustomItemHandler.TRAP_WAND.build(1),
                new ItemStack(Material.COBWEB, 32),
                new ItemStack(Material.COBBLESTONE, 64),
                new ItemStack(Material.WATER_BUCKET),
        };
    }
}

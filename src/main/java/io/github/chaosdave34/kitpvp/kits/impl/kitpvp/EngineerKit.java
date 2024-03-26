package io.github.chaosdave34.kitpvp.kits.impl.kitpvp;

import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EngineerKit extends Kit {
    public EngineerKit() {
        super("engineer", "Engineer");
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
                CustomItemHandler.TURRET.build(),
                CustomItemHandler.TRAP_WAND.build(),
                new ItemStack(Material.WATER_BUCKET),
        };
    }

    @Override
    public ItemStack[] getKillRewards() {
        return new ItemStack[]{
                new ItemStack(Material.COBWEB, 8),
                new ItemStack(Material.COBBLESTONE, 32),
        };
    }
}

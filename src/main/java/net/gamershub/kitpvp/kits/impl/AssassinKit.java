package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.companions.Companion;
import net.gamershub.kitpvp.companions.CompanionHandler;
import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AssassinKit extends Kit {
    public AssassinKit() {
        super("assassin", "Assassin");
    }

    @Override
    public ItemStack getHeadContent() {
        return new ItemStack(Material.CHAINMAIL_HELMET);
    }

    @Override
    public ItemStack getChestContent() {
        return new ItemStack(Material.IRON_CHESTPLATE);
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
        return new ItemStack[]{
                CustomItemHandler.ASSASSIN_SWORD.build(),
                CustomItemHandler.HAUNT_WAND.build(),
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

    @Override
    public Companion getCompanion() {
        return CompanionHandler.ALLAY;
    }
}

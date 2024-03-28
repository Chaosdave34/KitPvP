package io.github.chaosdave34.kitpvp.kits.impl.kits;

import io.github.chaosdave34.kitpvp.companions.Companion;
import io.github.chaosdave34.kitpvp.companions.CompanionHandler;
import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.Kit;
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

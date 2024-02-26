package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.inventory.ItemStack;

public class ZeusKit extends Kit {
    public ZeusKit() {
        super("zeus", "Zeus");
    }

    @Override
    public ItemStack[] getInventoryContent() {
        return new ItemStack[]{CustomItemHandler.LIGHTNING_WAND.build(1)};
    }
}

package io.github.chaosdave34.kitpvp.kits.impl.elytra;

import io.github.chaosdave34.kitpvp.kits.ElytraKit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TankKit extends ElytraKit {
    public TankKit() {
        super("elytra_tank", "Tank", Material.IRON_CHESTPLATE);
    }

    @Override
    public double getMaxHealth() {
        return 30;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);

        return new ItemStack[]{
                bow,
        };

    }
}

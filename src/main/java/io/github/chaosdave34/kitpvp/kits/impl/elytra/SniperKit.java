package io.github.chaosdave34.kitpvp.kits.impl.elytra;

import io.github.chaosdave34.kitpvp.kits.ElytraKit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SniperKit extends ElytraKit {
    public SniperKit() {
        super("elytra_sniper", "Sniper", Material.BOW);
    }

    @Override
    public double getMaxHealth() {
        return 10;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);

        return new ItemStack[]{
                bow,
        };
    }
}

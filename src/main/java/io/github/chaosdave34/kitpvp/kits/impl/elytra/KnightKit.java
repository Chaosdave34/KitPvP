package io.github.chaosdave34.kitpvp.kits.impl.elytra;

import io.github.chaosdave34.kitpvp.kits.ElytraKit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class KnightKit extends ElytraKit {
    public KnightKit() {
        super("elytra_knight", "Knight", Material.STONE_SWORD);
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 3);

        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
        stoneSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);

        return new ItemStack[]{
                bow,
                stoneSword,
        };
    }
}

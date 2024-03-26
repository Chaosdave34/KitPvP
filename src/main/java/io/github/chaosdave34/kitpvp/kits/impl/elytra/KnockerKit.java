package io.github.chaosdave34.kitpvp.kits.impl.elytra;

import io.github.chaosdave34.kitpvp.kits.ElytraKit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class KnockerKit extends ElytraKit {
    public KnockerKit() {
        super("elytra_knocker", "Knocker", Material.STICK);
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addEnchantment(Enchantment.ARROW_INFINITE,1 );
        bow.addEnchantment(Enchantment.ARROW_DAMAGE,  3);
        bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 10);

        return new ItemStack[]{
                bow,
        };
    }
}

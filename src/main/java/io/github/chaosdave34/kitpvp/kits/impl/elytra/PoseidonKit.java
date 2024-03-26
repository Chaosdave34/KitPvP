package io.github.chaosdave34.kitpvp.kits.impl.elytra;

import io.github.chaosdave34.kitpvp.kits.ElytraKit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class PoseidonKit extends ElytraKit {
    public PoseidonKit() {
        super("elytra_poseidon", "Poseidon", Material.TRIDENT);
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack trident = new ItemStack(Material.TRIDENT);
        trident.addEnchantment(Enchantment.LOYALTY, 3);

        ItemStack bogen = new ItemStack(Material.BOW);
        bogen.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        bogen.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        return new ItemStack[]{
                trident,
                bogen,
        };
    }
}

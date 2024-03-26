package io.github.chaosdave34.kitpvp.kits.impl.elytra;

import io.github.chaosdave34.kitpvp.kits.ElytraKit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class RocketLauncherKit extends ElytraKit {
    public RocketLauncherKit() {
        super("elytra_rocket_launcher", "Rocket Launcher", Material.CROSSBOW);
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        crossbow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        crossbow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 5);

        return new ItemStack[]{
                crossbow,
        };
    }
}

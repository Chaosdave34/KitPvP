package net.gamershub.kitpvp.enchantments;

import lombok.Getter;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.ReflectionUtils;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.enchantments.impl.BackstabEnchantment;
import net.gamershub.kitpvp.enchantments.impl.FreezeEnchantment;
import net.gamershub.kitpvp.enchantments.impl.LifeStealEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.craftbukkit.v1_20_R3.enchantments.CraftEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;

@Getter
public class CustomEnchantmentHandler implements Listener {
    public static Enchantment FREEZE;
    public static Enchantment LIFE_STEAL;
    public static Enchantment BACKSTAB;

    public CustomEnchantmentHandler() {
        // Unfreeze registry
        KitPvpPlugin.INSTANCE.getLogger().info("Enabling custom enchantment registering.");
        ReflectionUtils.unfreezeRegistry(BuiltInRegistries.ENCHANTMENT);

        FREEZE = registerEnchantment(new FreezeEnchantment());
        LIFE_STEAL = registerEnchantment(new LifeStealEnchantment());
        BACKSTAB = registerEnchantment(new BackstabEnchantment());

        // Freeze registry
        BuiltInRegistries.ENCHANTMENT.freeze();
    }

    public Enchantment registerEnchantment(CustomEnchantment customEnchantment) {
        Registry.register(BuiltInRegistries.ENCHANTMENT, customEnchantment.id, customEnchantment);
        Utils.registerEvents(customEnchantment);

        return CraftEnchantment.minecraftToBukkit(customEnchantment);
    }
}

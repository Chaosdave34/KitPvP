package net.gamershub.kitpvp.enchantments;

import lombok.Getter;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.enchantments.impl.FreezeEnchantment;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.craftbukkit.v1_20_R3.enchantments.CraftEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

@Getter
public class EnchantmentHandler implements Listener {
    private final List<Enchantment> bukkitEnchantments = new ArrayList<>();

    public static Enchantment FREEZE;

    public EnchantmentHandler() {
        enableRegistering();

        FREEZE = registerEnchantment(new FreezeEnchantment());

        disableRegistering();
    }

    private void enableRegistering() {
        KitPvpPlugin.INSTANCE.getLogger().info("Enabling custom enchantment registering.");
        try {
            for (Field field : BuiltInRegistries.ENCHANTMENT.getClass().getDeclaredFields()) {
                // private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders
                if (field.getName().equals("unregisteredIntrusiveHolders") || field.getName().equals("m")) {
                    field.setAccessible(true);
                    field.set(BuiltInRegistries.ENCHANTMENT, new IdentityHashMap<>());
                    field.setAccessible(false);
                }
                // private boolean frozen
                else if  (field.getName().equals("frozen") || field.getName().equals("l")) {
                    field.setAccessible(true);
                    field.set(BuiltInRegistries.ENCHANTMENT, false);
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            KitPvpPlugin.INSTANCE.getLogger().warning("Error while enabling custom enchantment registering.");
        }
    }

    public void disableRegistering() {
        BuiltInRegistries.ENCHANTMENT.freeze();
    }

    public Enchantment registerEnchantment(CustomEnchantment customEnchantment) {
        Registry.register(BuiltInRegistries.ENCHANTMENT, customEnchantment.name, customEnchantment);
        Utils.registerEvents(customEnchantment);

        Enchantment bukkitEnchantment = CraftEnchantment.minecraftToBukkit(customEnchantment);
        bukkitEnchantments.add(bukkitEnchantment);
        return bukkitEnchantment;
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent e) {
//        Player p = (Player) e.getPlayer();
//        PlayerInventory inventory = p.getInventory();
//        int slot = 0;
//        for (ItemStack itemStack : inventory.getContents()) {
//            slot++;
//            if (itemStack == null) continue;
//            if (itemStack.getEnchantments().keySet().stream().anyMatch(bukkitEnchantments::contains)) {
//                inventory.setItem(slot - 1, Utils.setLoreForItemsWithCustomEnchantment(itemStack));
//            }
//        }
    }


}

package io.github.chaosdave34.kitpvp.enchantments;

import io.github.chaosdave34.ghutils.enchantment.CustomEnchantment;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class BackstabEnchantment extends CustomEnchantment {
    public BackstabEnchantment() {
        super("backstab", 4, "Backstab", ItemTags.WEAPON_ENCHANTABLE, EquipmentSlot.MAINHAND);
    }

    @EventHandler
    public void onDealDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager) {
            ItemStack item = damager.getInventory().getItemInMainHand();
            if (item.containsEnchantment(CustomEnchantments.BACKSTAB)) {
                if (Math.abs(damager.getLocation().getDirection().angle(e.getEntity().getLocation().getDirection()) * 180 / Math.PI) < 30) {
                    e.setDamage(e.getDamage() * (1 + (item.getEnchantmentLevel(CustomEnchantments.BACKSTAB) * 0.25)));
                }
            }
        }
    }
}

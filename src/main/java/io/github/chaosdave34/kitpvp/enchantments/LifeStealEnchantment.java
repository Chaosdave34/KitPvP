package io.github.chaosdave34.kitpvp.enchantments;

import io.github.chaosdave34.ghutils.enchantment.CustomEnchantment;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class LifeStealEnchantment extends CustomEnchantment {
    public LifeStealEnchantment() {
        super("life_steal", 5, "Life Steal", ItemTags.WEAPON_ENCHANTABLE, EquipmentSlot.MAINHAND);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager) {
            ItemStack item = damager.getInventory().getItemInMainHand();
            if (item.containsEnchantment(CustomEnchantments.LIFE_STEAL)) {


                AttributeInstance maxHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                if (maxHealth == null) return;
                double maxHealthValue = maxHealth.getValue();

                double currentHealth = damager.getHealth();

                double heal = e.getDamage() * item.getEnchantmentLevel(CustomEnchantments.LIFE_STEAL) * 0.1;

                damager.setHealth(Math.min(currentHealth + heal, maxHealthValue));
            }
        }
    }
}

package net.gamershub.kitpvp.enchantments.impl;

import net.gamershub.kitpvp.enchantments.CustomEnchantment;
import net.gamershub.kitpvp.enchantments.EnchantmentHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LifeStealEnchantment extends CustomEnchantment {
    public LifeStealEnchantment() {
        super("life_steal", 5, "Life Steal", EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager) {
            if (damager.getInventory().getItemInMainHand().containsEnchantment(EnchantmentHandler.LIFE_STEAL)) {


                AttributeInstance maxHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                if (maxHealth == null) return;
                double maxHealthValue = maxHealth.getValue();

                double currentHealth = damager.getHealth();

                double heal = e.getDamage() * maxLevel * 0.1;

                damager.setHealth(Math.min(currentHealth + heal, maxHealthValue));
            }
        }
    }
}

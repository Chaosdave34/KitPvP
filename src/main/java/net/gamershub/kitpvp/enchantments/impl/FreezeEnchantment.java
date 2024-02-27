package net.gamershub.kitpvp.enchantments.impl;

import net.gamershub.kitpvp.enchantments.CustomEnchantment;
import net.gamershub.kitpvp.enchantments.EnchantmentHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FreezeEnchantment extends CustomEnchantment {
    public FreezeEnchantment() {
        super("freeze", 3, "Freeze", EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (e.getDamager() instanceof Player damager) {
                ItemStack itemStack = damager.getInventory().getItemInMainHand();
                if (itemStack.containsEnchantment(EnchantmentHandler.FREEZE)) {
                    int lvl = itemStack.getEnchantmentLevel(EnchantmentHandler.FREEZE);
                    if (e.getEntity() instanceof LivingEntity entity) {
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, lvl * 20, 1, false, false));
                    }
                }
            }
        }
    }
}

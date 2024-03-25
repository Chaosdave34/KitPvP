package io.github.chaosdave34.kitpvp.enchantments;

import io.github.chaosdave34.ghutils.enchantment.CustomEnchantment;
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
                if (itemStack.containsEnchantment(CustomEnchantments.FREEZE)) {
                    int lvl = itemStack.getEnchantmentLevel(CustomEnchantments.FREEZE);
                    if (e.getEntity() instanceof LivingEntity entity) {
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, lvl * 20, 1, false, false));
                    }
                }
            }
        }
    }
}

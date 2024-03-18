package net.gamershub.kitpvp.customentities.impl;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.customentities.CustomEntity;
import net.gamershub.kitpvp.pathfindergoals.TurretRangedAttackGoal;
import net.gamershub.kitpvp.persistentdatatypes.UUIDPersistentDataType;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftMob;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Turret extends CustomEntity {
    public Turret() {
        super("turret");
    }

    public void spawn(Player p, Location location) {
        p.getWorld().spawn(location, Husk.class, husk -> {
            husk.customName(Component.text("Turret 20/20"));
            husk.setCustomNameVisible(true);

            husk.setBaby();
            husk.setSilent(true);

            AttributeInstance maxHealthAttribute = husk.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealthAttribute != null) maxHealthAttribute.setBaseValue(20);

            AttributeInstance kockbackResistanceAttribute = husk.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            if (kockbackResistanceAttribute != null) kockbackResistanceAttribute.setBaseValue(1);

            husk.setHealth(20);

            PersistentDataContainer container = husk.getPersistentDataContainer();
            container.set(new NamespacedKey(KitPvpPlugin.INSTANCE, "owner"), new UUIDPersistentDataType(), p.getUniqueId());
            container.set(new NamespacedKey(KitPvpPlugin.INSTANCE, "custom_entity"), PersistentDataType.STRING, "turret");
        });

    }

    @Override
    public void onAddToWorld(EntityAddToWorldEvent e) {
        if (e.getEntity() instanceof Mob mob) {
            net.minecraft.world.entity.Mob nmsMob = ((CraftMob) mob).getHandle();

            final UUID turretOwnerUUUID;
            PersistentDataContainer container = mob.getPersistentDataContainer();
            NamespacedKey ownerKey = new NamespacedKey(KitPvpPlugin.INSTANCE, "owner");
            if (container.has(ownerKey)) {
                turretOwnerUUUID = container.get(ownerKey, new UUIDPersistentDataType());
            } else {
                turretOwnerUUUID = null;
            }

            nmsMob.goalSelector.removeAllGoals(goal -> true);
            nmsMob.goalSelector.addGoal(1, new RandomLookAroundGoal(nmsMob));
            nmsMob.goalSelector.addGoal(1, new TurretRangedAttackGoal(nmsMob, 20, 10.0F));

            nmsMob.targetSelector.removeAllGoals(goal -> true);
            nmsMob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(nmsMob, net.minecraft.world.entity.player.Player.class, 10, true, true, entity -> !entity.getUUID().equals(turretOwnerUUUID)));

            new BukkitRunnable() {
                @Override
                public void run() {
                    mob.damage(1);

                    if (mob.isDead() || !mob.isValid()) this.cancel();
                }
            }.runTaskTimer(KitPvpPlugin.INSTANCE, 0, 20 * 5);
        }
    }


    @EventHandler
    public void onTurretDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Monster entity) {
            if (checkCustomEntity(entity)) {


                if (e.getDamage() == Float.MAX_VALUE) return;

                if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent) {
                    PersistentDataContainer container = entity.getPersistentDataContainer();
                    NamespacedKey ownerKey = new NamespacedKey(KitPvpPlugin.INSTANCE, "owner");
                    if (container.has(ownerKey)) {
                        UUID turretOwnerUUUID = container.get(ownerKey, new UUIDPersistentDataType());
                        if (damageEvent.getDamager().getUniqueId().equals(turretOwnerUUUID)) {
                            e.setCancelled(true);
                        }
                    }
                }

                e.setDamage(1);
                entity.customName(Component.text("Turret " + Math.round(entity.getHealth()) + "/20"));
            }
        }
    }
}

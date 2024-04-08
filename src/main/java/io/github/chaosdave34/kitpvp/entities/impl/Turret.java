package io.github.chaosdave34.kitpvp.entities.impl;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import io.github.chaosdave34.ghutils.entity.CustomEntity;
import io.github.chaosdave34.ghutils.utils.PDCUtils;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.events.EntityReceiveDamageByEntityEvent;
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent;
import io.github.chaosdave34.kitpvp.pathfindergoals.TurretRangedAttackGoal;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftMob;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Turret extends CustomEntity {
    private final Map<UUID, UUID> turrets = new HashMap<>();

    public Turret() {
        super("turret");
    }

    public void spawn(Player p, Location location) {
        Husk turret = p.getWorld().spawn(location, Husk.class, husk -> {
            husk.customName(Component.text("Turret 10/10"));
            husk.setCustomNameVisible(true);

            husk.setBaby();
            husk.setSilent(true);

            AttributeInstance maxHealthAttribute = husk.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealthAttribute != null) maxHealthAttribute.setBaseValue(10);

            AttributeInstance kockbackResistanceAttribute = husk.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            if (kockbackResistanceAttribute != null) kockbackResistanceAttribute.setBaseValue(1);

            husk.setHealth(10);

            PDCUtils.setOwner(husk, p.getUniqueId());

            PersistentDataContainer container = husk.getPersistentDataContainer();
            container.set(new NamespacedKey(KitPvp.INSTANCE, "custom_entity"), PersistentDataType.STRING, "turret");
        });
        turrets.put(p.getUniqueId(), turret.getUniqueId());
    }

    @Override
    public void onAddToWorld(EntityAddToWorldEvent e) {
        if (e.getEntity() instanceof Mob mob) {
            net.minecraft.world.entity.Mob nmsMob = ((CraftMob) mob).getHandle();

            UUID turretOwnerUUUID = PDCUtils.getOwner(mob);

            nmsMob.goalSelector.removeAllGoals(goal -> true);
            nmsMob.goalSelector.addGoal(1, new RandomLookAroundGoal(nmsMob));
            nmsMob.goalSelector.addGoal(1, new TurretRangedAttackGoal(nmsMob, 20, 20.0F));

            nmsMob.targetSelector.removeAllGoals(goal -> true);
            nmsMob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(nmsMob, net.minecraft.world.entity.player.Player.class, 10, true, true, entity -> !entity.getUUID().equals(turretOwnerUUUID)));

            new BukkitRunnable() {
                @Override
                public void run() {
                    mob.damage(1);

                    if (mob.isDead() || !mob.isValid()) this.cancel();
                }
            }.runTaskTimer(KitPvp.INSTANCE, 0, 20 * 5);
        }
    }

    @EventHandler
    public void onDamageByOwner(EntityReceiveDamageByEntityEvent e) {
        if (e.getEntity() instanceof org.bukkit.entity.LivingEntity entity) {
            if (checkCustomEntity(entity)) {
                if (e.getDamager() instanceof Player p) {
                    if (p.getUniqueId().equals(PDCUtils.getOwner(entity))) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof org.bukkit.entity.LivingEntity entity) {
            if (checkCustomEntity(entity)) {
                if (e.getDamage() == Float.MAX_VALUE) return;

                if (e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                    e.setCancelled(true);
                    return;
                }

                e.setDamage(1);
                entity.customName(Component.text("Turret " + Math.round(entity.getHealth()) + "/10"));
            }
        }
    }

    public void performRangedAttack(Mob mob, LivingEntity target, float pullProgress) {
        org.bukkit.inventory.ItemStack rocketItem = new org.bukkit.inventory.ItemStack(Material.FIREWORK_ROCKET);
        rocketItem.editMeta(FireworkMeta.class, fireworkMeta -> {
            fireworkMeta.setPower(5);
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.BURST).build());
        });

        CraftItemStack.asNMSCopy(rocketItem);

        double d0 = target.getEyeY() - 1.100000023841858D;
        double d1 = target.getX() - mob.getX();
        double d2 = d0 - mob.getEyeLocation().getY();
        double d3 = target.getZ() - mob.getZ();
        float speed = 1.6F;

        Vector velocity = new Vector(d1, d2, d3).normalize().multiply(speed);


        mob.getWorld().spawnEntity(mob.getEyeLocation(), EntityType.FIREWORK, CreatureSpawnEvent.SpawnReason.CUSTOM, entity -> {
            Firework firework = (Firework) entity;

            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            fireworkMeta.setPower(3);
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.BLACK).withColor(Color.RED).with(FireworkEffect.Type.BURST).build());
            firework.setFireworkMeta(fireworkMeta);

            firework.setShotAtAngle(true);
            firework.setTicksToDetonate(60);
            firework.setShooter(mob);
            firework.setVelocity(velocity);
        });
        mob.getWorld().playSound(mob.getLocation(), Sound.ENTITY_GENERIC_WIND_BURST, 1.0F, 0.4F);
    }

    @EventHandler
    public void onOwnerDeath(PlayerSpawnEvent e) {
        Player p = e.getPlayer();
        if (turrets.containsKey(p.getUniqueId())) {
            Entity entity = Bukkit.getEntity(turrets.get(p.getUniqueId()));
            if (entity != null) entity.remove();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (turrets.containsKey(p.getUniqueId())) {
            Entity entity = Bukkit.getEntity(turrets.get(p.getUniqueId()));
            if (entity != null) entity.remove();
        }
    }
}

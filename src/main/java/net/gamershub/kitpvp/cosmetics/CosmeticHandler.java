package net.gamershub.kitpvp.cosmetics;

import lombok.Getter;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.cosmetics.impl.ShriekKillEffect;
import net.gamershub.kitpvp.cosmetics.impl.SimpleKillEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CosmeticHandler implements Listener {
    public Map<String, ProjectileTrail> projectileTrails = new HashMap<>();
    public Map<Integer, Integer> activeProjectiles = new HashMap<>();

    public Map<String, KillEffect> killEffects = new HashMap<>();

    public static ProjectileTrail HEARTS_PROJECTILE_TRAIL;
    public static ProjectileTrail COMPOSTER_PROJECTILE_TRAIL;
    public static ProjectileTrail ANGRY_VILLAGER_PROJECTILE_TRAIL;
    public static ProjectileTrail BUBBLE_PROJECTILE_TRAIL;
    public static ProjectileTrail CHERRY_LEAVES_PROJECTILE_TRAIL;
    public static ProjectileTrail ELECTRIC_SPARK_PROJECTILE_TRAIL;
    public static ProjectileTrail ENCHANT_PROJECTILE_TRAIL;
    public static ProjectileTrail FLAME_PROJECTILE_TRAIL;
    public static ProjectileTrail GLOW_PROJECTILE_TRAIL;
    public static ProjectileTrail NAUTILUS_PROJECTILE_TRAIL;
    public static ProjectileTrail NOTE_PROJECTILE_TRAIL;
    public static ProjectileTrail SOUL_FIRE_PROJECTILE_TRAIL;
    public static ProjectileTrail TOTEM_OF_UNDYING_PROJECTILE_TRAIL;
    public static ProjectileTrail SNOWFLAKE_PROJECTILE_TRAIL;
    public static ProjectileTrail SOUL_PROJECTILE_TRAIL;
    public static ProjectileTrail SCULK_SOUL_PROJECTILE_TRAIL;
    public static ProjectileTrail SCULK_CHARGE_PROJECTILE_TRAIL;
    public static ProjectileTrail END_ROD_PROJECTILE_TRAIL;

    public static KillEffect SHRIEK_KILL_EFFECT;
    public static KillEffect SMOKE_KILL_EFFECT;
    public static KillEffect SONIC_BOOM_KILL_EFFECT;
    public static KillEffect GUST_KILL_EFFECT;
    public static KillEffect SQUID_INK_KILL_EFFECT;
    public static KillEffect GLOW_SQUID_INK_KILL_EFFECT;
    public static KillEffect EXPLOSION_KILL_EFFECT;

    public CosmeticHandler() {
        HEARTS_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("hearts", "Hearts", Particle.HEART, 1, Material.RED_DYE));
        COMPOSTER_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("composter", "Composter", Particle.COMPOSTER, 2, Material.COMPOSTER));
        ANGRY_VILLAGER_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("angry_villager", "Angry Villager", Particle.VILLAGER_ANGRY, 3, Material.VILLAGER_SPAWN_EGG));
        BUBBLE_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("bubble", "Bubble", Particle.BUBBLE_POP, 4, Material.WATER_BUCKET));
        CHERRY_LEAVES_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("cherry_blossom", "Cherry Blossom", Particle.CHERRY_LEAVES, 5, Material.CHERRY_LEAVES));
        ELECTRIC_SPARK_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("electric_spark", "Electric Spark", Particle.ELECTRIC_SPARK, 6, Material.LIGHTNING_ROD));
        ENCHANT_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("enchant", "Enchant", Particle.ENCHANTMENT_TABLE, 7, Material.ENCHANTING_TABLE));
        FLAME_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("flame", "Flame", Particle.SMALL_FLAME, 8, Material.TORCH));
        GLOW_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("glow", "Glow", Particle.GLOW, 9, Material.GLOW_INK_SAC));
        NAUTILUS_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("nautilus", "Nautilus", Particle.NAUTILUS, 10, Material.NAUTILUS_SHELL));
        NOTE_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("note", "Note", Particle.NOTE, 11, Material.NOTE_BLOCK));
        SOUL_FIRE_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("soul_fire", "Soul Fire", Particle.SOUL_FIRE_FLAME, 12, Material.SOUL_LANTERN));
        TOTEM_OF_UNDYING_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("totem_of_undying", "Totem of Undying", Particle.TOTEM, 13, Material.TOTEM_OF_UNDYING));
        SNOWFLAKE_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("snowflake", "Snowflake", Particle.SNOWFLAKE, 14, Material.SNOWBALL));
        SOUL_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("soul", "Soul", Particle.SOUL, 15, Material.SOUL_SAND));
        SCULK_SOUL_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("sculk_soul", "Sculk Soul", Particle.SCULK_SOUL, 16, Material.SCULK_CATALYST));
        SCULK_CHARGE_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("sculk_charge", "Sculk Charge", Particle.SCULK_CHARGE_POP, 17, Material.SCULK));
        END_ROD_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("end_rod", "End Rod", Particle.END_ROD, 18, Material.END_ROD));

        SMOKE_KILL_EFFECT = registerKillEffect(new SimpleKillEffect("smoke", "Smoke", Particle.SMOKE_NORMAL, 1, Material.COAL));
        SHRIEK_KILL_EFFECT = registerKillEffect(new ShriekKillEffect());
        SONIC_BOOM_KILL_EFFECT = registerKillEffect(new SimpleKillEffect("sonic_boom", "Sonic Boom", Particle.SMOKE_LARGE, 3, Material.WARDEN_SPAWN_EGG));
        GUST_KILL_EFFECT = registerKillEffect(new SimpleKillEffect("gust", "Gust", Particle.GUST, 4, Material.STONE_SWORD));
        SQUID_INK_KILL_EFFECT = registerKillEffect(new SimpleKillEffect("squid_ink", "Squid Ink", Particle.SQUID_INK, 5, Material.INK_SAC));
        GLOW_SQUID_INK_KILL_EFFECT = registerKillEffect(new SimpleKillEffect("glow_squid_ink", "Glow Squid Ink", Particle.GLOW_SQUID_INK, 6, Material.GLOW_INK_SAC));
        EXPLOSION_KILL_EFFECT = registerKillEffect(new SimpleKillEffect("explosion", "Explosion", Particle.EXPLOSION_NORMAL, 7, Material.TNT));
    }

    private ProjectileTrail registerProjectileTrail(ProjectileTrail projectileTrail) {
        projectileTrails.put(projectileTrail.getId(), projectileTrail);
        return projectileTrail;
    }

    private KillEffect registerKillEffect(KillEffect killEffect) {
        killEffects.put(killEffect.getId(), killEffect);
        return killEffect;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        if (e.getEntity().getShooter() instanceof Player p) {
            ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

            if (extendedPlayer.getProjectileTrailId() != null) {
                Particle particle = projectileTrails.get(extendedPlayer.getProjectileTrailId()).getParticle();

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        projectile.getWorld().spawnParticle(particle, projectile.getLocation(), 1, 0, 0, 0, 0);

                        if (projectile instanceof Firework firework) {
                            if (firework.isDetonated()) this.cancel();
                        }

                        if (!projectile.isValid() || projectile.isDead()) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(KitPvpPlugin.INSTANCE, 0, 2);

                activeProjectiles.put(projectile.getEntityId(), task.getTaskId());
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        if (activeProjectiles.containsKey(projectile.getEntityId())) {
            Bukkit.getScheduler().cancelTask(activeProjectiles.remove(projectile.getEntityId()));
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();
        if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent) {
            if (damageEvent.getDamager() instanceof Player damager) {
                ExtendedPlayer extendedDamager = KitPvpPlugin.INSTANCE.getExtendedPlayer(damager);
                if (extendedDamager.getKillEffectId() != null) {
                    killEffects.get(extendedDamager.getKillEffectId()).playEffect(entity.getLocation());
                }
            }
        }
    }
}

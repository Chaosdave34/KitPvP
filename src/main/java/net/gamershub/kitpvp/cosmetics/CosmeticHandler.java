package net.gamershub.kitpvp.cosmetics;

import lombok.Getter;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.cosmetics.impl.ShriekKillEffect;
import net.gamershub.kitpvp.cosmetics.impl.SimpleKillEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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

    public static KillEffect SHRIEK_KILL_EFFECT;
    public static KillEffect SMOKE_KILL_EFFECT;
    public static KillEffect SONIC_BOOM_KILL_EFFECT;
    public static KillEffect GUST_KILL_EFFECT;

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

        SMOKE_KILL_EFFECT = registerKillEffect(new SimpleKillEffect("smoke", "Smoke", Particle.SMOKE_NORMAL, 1, Material.COAL));
        SHRIEK_KILL_EFFECT = registerKillEffect(new ShriekKillEffect());
        SONIC_BOOM_KILL_EFFECT = registerKillEffect(new SimpleKillEffect("sonic_boom", "Sonic Boom", Particle.SMOKE_LARGE, 3, Material.WARDEN_SPAWN_EGG));
        GUST_KILL_EFFECT = registerKillEffect(new SimpleKillEffect("gust", "Gust", Particle.GUST, 4, Material.STONE_SWORD));
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

package net.gamershub.kitpvp.cosmetics;

import lombok.Getter;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.cosmetics.impl.ShriekKillEffect;
import net.gamershub.kitpvp.cosmetics.impl.SmokeKillEffect;
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

    public static KillEffect SHRIEK_KILL_EFFECT;
    public static KillEffect SMOKE_KILL_EFFECT;

    public CosmeticHandler() {
        HEARTS_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("hearts", "Hearts", Particle.HEART, 1, Material.RED_DYE));
        COMPOSTER_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("composter", "Composter", Particle.COMPOSTER, 10, Material.COMPOSTER));
        ANGRY_VILLAGER_PROJECTILE_TRAIL = registerProjectileTrail(new ProjectileTrail("angry_villager", "Angry Villager", Particle.VILLAGER_ANGRY, 20, Material.VILLAGER_SPAWN_EGG));

        SHRIEK_KILL_EFFECT = registerKillEffect(new ShriekKillEffect());
        SMOKE_KILL_EFFECT = registerKillEffect(new SmokeKillEffect());
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
                        projectile.getWorld().spawnParticle(particle, projectile.getLocation(), 1);

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

package net.gamershub.kitpvp.companions.pathfindergoals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class CustomOwnerHurtByTarget extends TargetGoal {
    private final Player owner;
    private int timestamp;

    public CustomOwnerHurtByTarget(Mob mob, Player owner) {
        super(mob, false);
        this.owner = owner;
    }

    @Override
    public boolean canUse() {
        int i = ((LivingEntity) ((CraftEntity) owner).getHandle()).getLastHurtByMobTimestamp();
        return i != this.timestamp;
    }

    @Override
    public void start() {
        if (owner.getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent) {
            Entity damager = damageEvent.getDamager();
            if (damager instanceof org.bukkit.entity.LivingEntity) {
                mob.setTarget((LivingEntity) ((CraftEntity) damageEvent.getDamager()).getHandle(), EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, true);
                timestamp =  ((LivingEntity) ((CraftEntity) owner).getHandle()).getLastHurtByMobTimestamp();
            } else if (damager instanceof Projectile projectile) {
                if (projectile.getShooter() instanceof LivingEntity) {
                    mob.setTarget((LivingEntity) ((CraftEntity) projectile.getShooter()).getHandle(), EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, true);
                }
            }
        }
        super.start();
    }
}

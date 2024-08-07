package io.github.chaosdave34.kitpvp.pathfindergoals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.EnumSet;

// Todo cleanup and improve code
public class TurretRangedAttackGoal extends Goal {
    private final Mob mob;
    @Nullable
    private net.minecraft.world.entity.LivingEntity target;
    private int attackTime = -1;
    private int seeTime;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;

    public TurretRangedAttackGoal(Mob mob, int intervalTicks, float maxShootRange) {
        this(mob, intervalTicks, intervalTicks, maxShootRange);
    }

    public TurretRangedAttackGoal(Mob mob, int minIntervalTicks, int maxIntervalTicks, float maxShootRange) {
        this.mob = mob;
        this.attackIntervalMin = minIntervalTicks;
        this.attackIntervalMax = maxIntervalTicks;
        this.attackRadius = maxShootRange;
        this.attackRadiusSqr = maxShootRange * maxShootRange;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }


    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity != null && livingEntity.isAlive()) {

            double d = this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            if (!(d > (double) this.attackRadiusSqr) && this.mob.hasLineOfSight(livingEntity)) {
                this.target = livingEntity;
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || (this.target != null && this.target.isAlive()) && !this.mob.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (target == null) return;
        double d = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean bl = this.mob.getSensing().hasLineOfSight(this.target);
        if (bl) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (!(d > (double) this.attackRadiusSqr) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            target = null;
            return;
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {

            float f = (float) Math.sqrt(d) / this.attackRadius;

            performRangedAttack(this.mob, this.target);

            this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d) / (double) this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
        }
    }

    public static void performRangedAttack(Mob mob, LivingEntity target) {
        ItemStack rocketItem = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta fireworkMeta = (FireworkMeta) rocketItem.getItemMeta();
        fireworkMeta.setPower(5);
        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).with(FireworkEffect.Type.BURST).build());
        rocketItem.setItemMeta(fireworkMeta);

        double d0 = target.getEyeY() - 1.100000023841858;
        double d1 = target.getX() - mob.getX();
        double d2 = d0 - mob.getEyeY();
        double d3 = target.getZ() - mob.getZ();
        float speed = 1.6f;

        Vector velocity = new Vector(d1, d2, d3).normalize().multiply(speed);

        Firework firework = mob.getBukkitMob().getWorld().spawn(mob.getBukkitMob().getEyeLocation(), Firework.class);
        fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.setPower(3);
        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.BLACK).withColor(Color.RED).with(FireworkEffect.Type.BURST).build());
        firework.setFireworkMeta(fireworkMeta);

        firework.setShotAtAngle(true);
        firework.setTicksToDetonate(60);
        firework.setShooter(mob.getBukkitMob());
        firework.setVelocity(velocity);

        mob.getBukkitMob().getWorld().playSound(mob.getBukkitMob().getLocation(), Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 0.4f);
    }
}

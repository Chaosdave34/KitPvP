package io.github.chaosdave34.kitpvp.pathfindergoals;

import io.github.chaosdave34.kitpvp.entities.CustomEntities;
import io.github.chaosdave34.kitpvp.entities.impl.Turret;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

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
            float g = Mth.clamp(f, 0.1F, 1.0F);

            ((Turret) CustomEntities.TURRET).performRangedAttack(this.mob.getBukkitMob(), this.target, g);


            this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d) / (double) this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
        }

    }


}

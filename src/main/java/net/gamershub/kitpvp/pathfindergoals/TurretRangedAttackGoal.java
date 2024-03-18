package net.gamershub.kitpvp.pathfindergoals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;

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

            performRangedAttack(this.target, g);


            this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d) / (double) this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
        }

    }

    public void performRangedAttack(LivingEntity target, float pullProgress) {
        Arrow arrowEntity = new Arrow(this.mob.level(), this.mob, ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.ARROW)));
        double d0 = target.getEyeY() - 1.100000023841858D;
        double d1 = target.getX() - this.mob.getX();
        double d2 = d0 - arrowEntity.getY();
        double d3 = target.getZ() - this.mob.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.20000000298023224D;

        arrowEntity.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);

        CraftWorld world = this.mob.level().getWorld();
        world.playSound(new Location(world, this.mob.getX(), this.mob.getY(), this.mob.getZ()), Sound.ENTITY_GENERIC_WIND_BURST, 1.0F, 0.4F);
        this.mob.level().addFreshEntity(arrowEntity);
    }
}

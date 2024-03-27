package io.github.chaosdave34.kitpvp.pathfindergoals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Player;

import java.util.EnumSet;

public class CustomOwnerHurtTarget extends TargetGoal {
    private final LivingEntity owner;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public CustomOwnerHurtTarget(Mob mob, Player owner) {
        super(mob, false);
        this.owner = ((CraftLivingEntity) owner).getHandle();
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (owner == null  || !owner.equals(owner.getLastHurtByMob())) {
            return false;
        } else {
            this.ownerLastHurt = owner.getLastHurtMob();
            int i = owner.getLastHurtMobTimestamp();

            return i != this.timestamp && this.canAttack(this.owner, TargetingConditions.DEFAULT);
        }
    }

    @Override
    public void start() {
        mob.setTarget(ownerLastHurt, org.bukkit.event.entity.EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, false);

        if (owner != null) {
            this.timestamp = owner.getLastHurtMobTimestamp();
        }

        super.start();
    }
}

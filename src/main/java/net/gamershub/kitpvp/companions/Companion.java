package net.gamershub.kitpvp.companions;

import net.gamershub.kitpvp.pathfindergoals.CustomFollowOwnerGoal;
import net.gamershub.kitpvp.pathfindergoals.CustomMeleeAttackGoal;
import net.gamershub.kitpvp.pathfindergoals.CustomOwnerHurtByTarget;
import net.gamershub.kitpvp.pathfindergoals.CustomOwnerHurtTarget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftVector;
import org.bukkit.entity.Player;

public abstract class Companion {
    protected final String name;
    protected final double attackDamage;

    public Companion(String name, int attackDamage) {
        this.name = name;
        this.attackDamage = attackDamage;
    }

    public abstract PathfinderMob createVanillaMob(Level world);

    public org.bukkit.entity.Mob createCompanion(Player owner) {
        PathfinderMob companion = createVanillaMob(((CraftWorld) owner.getLocation().getWorld()).getHandle());
        companion.setPos(CraftVector.toNMS(owner.getLocation().toVector()));
        companion.setInvulnerable(true);

        companion.getAttributes().registerAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance attribute = companion.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attribute != null) attribute.setBaseValue(attackDamage);

        companion.setCustomName(Component.literal(name));
        companion.setCustomNameVisible(true);

        for (WrappedGoal goal : companion.goalSelector.getAvailableGoals()) {
            companion.goalSelector.removeGoal(goal);
        }

        companion.goalSelector.addGoal(1, new FloatGoal(companion));
        companion.goalSelector.addGoal(2, new CustomMeleeAttackGoal(companion, owner, 1, false));
        companion.goalSelector.addGoal(3, new CustomFollowOwnerGoal(companion, owner, 1.0D, 10.0F, 2.0F, false));

        companion.targetSelector.removeAllGoals(testgoal -> true);

        companion.targetSelector.addGoal(1, new CustomOwnerHurtByTarget(companion, owner));
        companion.targetSelector.addGoal(2, new CustomOwnerHurtTarget(companion, owner));
        companion.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(companion, net.minecraft.world.entity.player.Player.class, 10, true, true, livingEntity -> !livingEntity.getUUID().equals(owner.getUniqueId())));


        return companion.getBukkitMob();
    }

    public PathfinderMob createDummyCompanion(Location location) {
        PathfinderMob companion = createVanillaMob(((CraftWorld) location.getWorld()).getHandle());
        companion.setPos(CraftVector.toNMS(location.toVector()));
        companion.setRot(location.getYaw(), location.getPitch());
        companion.setYHeadRot(location.getYaw());
        companion.setInvulnerable(true);
        companion.setNoAi(true);
        companion.setSilent(true);

        companion.setCustomName(Component.literal(name));
        companion.setCustomNameVisible(true);

        return companion;
    }
}

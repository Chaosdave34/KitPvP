package net.gamershub.kitpvp.companions;

import net.gamershub.kitpvp.companions.pathfindergoals.CustomOwnerHurtByTarget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.monster.warden.Warden;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.Player;

public class Companion extends Allay {
    final private Player owner;

    public Companion(Location location, Player owner) {
        super(EntityType.ALLAY, ((CraftWorld) location.getWorld()).getHandle());
        this.owner = owner;

        setPos(location.x(), location.y(), location.z());

        getAttributes().registerAttribute(Attributes.ATTACK_DAMAGE);

        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(10);

        setCustomName(Component.literal("Dangerous Allay"));

        for (WrappedGoal goal : goalSelector.getAvailableGoals()) {
            goalSelector.removeGoal(goal);
        }

        for (WrappedGoal goal : targetSelector.getAvailableGoals()) {
            targetSelector.removeGoal(goal);
        }

        goalSelector.addGoal(0, new MeleeAttackGoal(this, 1, false));
        //goalSelector.addGoal(1, new CustomFollowOwnerGoal(this, owner, 1));


        targetSelector.addGoal(0, new CustomOwnerHurtByTarget(this, owner));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Warden.class, false, false));
    }

}

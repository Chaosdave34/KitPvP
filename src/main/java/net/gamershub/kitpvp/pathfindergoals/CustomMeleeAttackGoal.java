package net.gamershub.kitpvp.pathfindergoals;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.bukkit.entity.Player;

public class CustomMeleeAttackGoal extends MeleeAttackGoal {
    private final Player owner;

    public CustomMeleeAttackGoal(PathfinderMob mob, Player owner, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
        this.owner = owner;
    }

    @Override
    public boolean canUse() {
        if (ExtendedPlayer.from(owner).inSpawn())
            return false;

        return super.canUse();
    }
}

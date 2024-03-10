package net.gamershub.kitpvp.companions.pathfindergoals;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
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
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(owner).getGameState() == ExtendedPlayer.GameState.SPAWN)
            return false;

        return super.canUse();
    }
}

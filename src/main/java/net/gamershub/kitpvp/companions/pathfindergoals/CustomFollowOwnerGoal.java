package net.gamershub.kitpvp.companions.pathfindergoals;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CustomFollowOwnerGoal extends Goal {
    private final Player owner;
    private final double speed;

    private final PathNavigation navigation;

    public CustomFollowOwnerGoal(Mob mob, Player owner, double speed) {
        this.owner = owner;
        this.speed = speed;

        this.navigation = mob.getNavigation();
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        navigation.moveTo(((CraftPlayer) owner).getHandle(), speed);
    }
}

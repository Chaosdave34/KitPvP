package net.gamershub.kitpvp.companions.impl;

import net.gamershub.kitpvp.companions.Companion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;

public class ZoglinCompanion extends Companion {
    public ZoglinCompanion() {
        super("Zombified Piglin", 5);
    }

    @Override
    public PathfinderMob createVanillaMob(Level world) {
        return new ZombifiedPiglin(EntityType.ZOMBIFIED_PIGLIN, world);
    }
}

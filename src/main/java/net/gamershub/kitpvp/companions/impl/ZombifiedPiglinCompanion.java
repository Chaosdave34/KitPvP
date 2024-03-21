package net.gamershub.kitpvp.companions.impl;

import net.gamershub.kitpvp.companions.Companion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;

public class ZombifiedPiglinCompanion extends Companion {
    public ZombifiedPiglinCompanion() {
        super("Zombified Piglin", 5);
    }

    @Override
    public PathfinderMob createVanillaMob(Level world) {
        ZombifiedPiglin companion = new ZombifiedPiglin(EntityType.ZOMBIFIED_PIGLIN, world);
        companion.setBaby(true);
        return companion;
    }
}

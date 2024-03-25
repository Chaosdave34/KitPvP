package io.github.chaosdave34.kitpvp.companions.impl;

import io.github.chaosdave34.kitpvp.companions.Companion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.Level;

public class AllayCompanion extends Companion {
    public AllayCompanion() {
        super("Allay", 5);
    }

    @Override
    public PathfinderMob createVanillaMob(Level world) {
        return new Allay(EntityType.ALLAY, world);
    }
}

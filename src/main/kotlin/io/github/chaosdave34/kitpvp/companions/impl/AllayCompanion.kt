package io.github.chaosdave34.kitpvp.companions.impl

import io.github.chaosdave34.kitpvp.companions.Companion
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.animal.allay.Allay
import net.minecraft.world.level.Level

class AllayCompanion : Companion("Allay", 5.0) {
    override fun createVanillaMob(world: Level): PathfinderMob {
        return Allay(EntityType.ALLAY, world)
    }
}
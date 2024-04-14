package io.github.chaosdave34.kitpvp.companions.impl

import io.github.chaosdave34.kitpvp.companions.Companion
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.monster.ZombifiedPiglin
import net.minecraft.world.level.Level

class ZombifiedPiglinCompanion : Companion("Zombified Piglin", 5.0) {
    override fun createVanillaMob(world: Level): PathfinderMob {
        val companion = ZombifiedPiglin(EntityType.ZOMBIFIED_PIGLIN, world)
        companion.isBaby = true
        return companion
    }
}
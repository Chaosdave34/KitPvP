package io.github.chaosdave34.kitpvp.textdisplays

import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Display.TextDisplay
import net.minecraft.world.entity.EntityType

class NMSCustomTextDisplay(world: ServerLevel): TextDisplay(EntityType.TEXT_DISPLAY, world) {
    lateinit var serverEntity: ServerEntity
}
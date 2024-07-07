package io.github.chaosdave34.kitpvp.companions

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.companions.impl.AllayCompanion
import io.github.chaosdave34.kitpvp.companions.impl.ZombifiedPiglinCompanion
import io.github.chaosdave34.kitpvp.events.EntityReceiveDamageByEntityEvent
import io.github.chaosdave34.kitpvp.extensions.getPDCOwner
import org.bukkit.Sound
import org.bukkit.entity.Pose
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class CompanionHandler : Listener {
    companion object {
        lateinit var ALLAY: io.github.chaosdave34.kitpvp.companions.Companion
        lateinit var ZOMBIFIED_PIGLIN: io.github.chaosdave34.kitpvp.companions.Companion
    }

    init {
        ALLAY = registerCompanion(AllayCompanion())
        ZOMBIFIED_PIGLIN = registerCompanion(ZombifiedPiglinCompanion())
    }

    private fun registerCompanion(companion: io.github.chaosdave34.kitpvp.companions.Companion): io.github.chaosdave34.kitpvp.companions.Companion {
        return companion
    }

    @EventHandler
    fun onCompanionDeath(e: EntityDeathEvent) {
        val entity = e.entity
        if (entity.hasMetadata("companion")) {
            e.isCancelled = true

            entity.world.playSound(net.kyori.adventure.sound.Sound.sound(Sound.ENTITY_GENERIC_DEATH, net.kyori.adventure.sound.Sound.Source.HOSTILE, 1f, 1f))

            entity.setAI(false)
            entity.isSilent = true
            entity.pose = Pose.SLEEPING
            entity.isInvulnerable = true
        }
    }

    @EventHandler
    fun onCompanionDamage(event: EntityReceiveDamageByEntityEvent) {
        if (event.entity.hasMetadata("companion")) {
            val ownerUUID = event.entity.getPDCOwner()

            if (ownerUUID != null && ExtendedPlayer.from(ownerUUID).inSpawn()) event.isCancelled = true

            if (event.damager.uniqueId == ownerUUID) event.isCancelled = true
        }
    }
}
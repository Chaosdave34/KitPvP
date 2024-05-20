package io.github.chaosdave34.kitpvp.ultimates

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.utils.Describable
import net.kyori.adventure.text.Component
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Listener

abstract class Ultimate(val id: String, val name: String, val damageRequired: Double) : Listener, Describable {
    abstract fun getDescription(): Component

    abstract fun onAbility(player: Player)

    fun getProgress(player: Player): Float {
        return KitPvp.INSTANCE.ultimateHandler.getProgress(player, this)
    }

    protected fun Entity.checkTargetIfPlayer(): Boolean = (this is Player && ExtendedPlayer.from(this).inGame()) || this !is Player
}
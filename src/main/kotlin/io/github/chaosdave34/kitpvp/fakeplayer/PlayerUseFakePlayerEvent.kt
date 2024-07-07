package io.github.chaosdave34.kitpvp.fakeplayer

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent
import org.bukkit.inventory.EquipmentSlot

class PlayerUseFakePlayerEvent(fakePlayer: FakePlayer, event: PlayerUseUnknownEntityEvent) :
    PlayerUseUnknownEntityEvent(event.player, fakePlayer.entityId, event.isAttack, event.hand, event.clickedRelativePosition) {
    var isActualInteract = false

    init {
        isActualInteract = (clickedRelativePosition == null && hand == EquipmentSlot.HAND)
    }
}
package io.github.chaosdave34.kitpvp.abilities.impl

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent

class ExplodeAbility : Ability("explode", "Explode", Type.SNEAK, 15) {

    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Cause an explosion.")

    override fun onAbility(player: Player): Boolean {
        var power = 2f
        if (player.isGlowing) {
            player.isGlowing = false
            power *= 2
        }
        player.location.createExplosion(player, power, false)
        return true
    }

    @EventHandler
    fun onExplode(event: EntityExplodeEvent) {
        val player = event.entity
        if (player is Player) {
            for (armorContent in player.inventory.armorContents) {
                if (armorContent?.hasThisAbility() == true) {
                    event.blockList().removeIf { block: Block -> !block.hasMetadata("placed_by_player") }
                    return
                }
            }
        }
    }
}
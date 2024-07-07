package io.github.chaosdave34.kitpvp.abilities.impl

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

class ExplodeAbility : Ability("explode", "Explode", 15, 50, Material.TNT) {

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

    // Todo: disable damage for executing player
//    @EventHandler
//    fun onExplode(event: EntityExplodeEvent) {
//        val player = event.entity
//        if (player is Player) {
//            for (armorContent in player.inventory.armorContents) {
//                if (armorContent?.hasThisAbility() == true) {
//                    event.blockList().removeIf { block: Block -> !block.hasMetadata("placed_by_player") }
//                    return
//                }
//            }
//        }
//    }
}
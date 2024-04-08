package io.github.chaosdave34.kitpvp.abilities.impl.magician

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player

class ShuffleAbility : Ability("shuffle", "Shuffle", Type.SNEAK_RIGHT_CLICK, 10) {
    override fun getDescription(): List<Component> = createSimpleDescription("Shuffles the inventory the enemy you are looking at in a 8 block radius.")

    override fun onAbility(player: Player): Boolean {
        val target = player.getTargetEntity(8)
        if (target is HumanEntity && target.checkTargetIfPlayer()) {
            val inventory = target.inventory
            val shuffledContent = mutableListOf(*inventory.contents)
            shuffledContent.shuffle()
            inventory.contents = shuffledContent.toTypedArray()
            return true
        }
        return false
    }
}
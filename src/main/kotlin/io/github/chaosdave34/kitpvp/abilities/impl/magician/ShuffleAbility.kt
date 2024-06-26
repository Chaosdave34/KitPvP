package io.github.chaosdave34.kitpvp.abilities.impl.magician

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player

class ShuffleAbility : Ability("shuffle", "Shuffle", Type.RIGHT_CLICK, 10) {
    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Shuffles the inventory the enemy you are looking at in a 8 block radius.")

    override fun onAbility(player: Player): Boolean {
        val target = player.getTargetEntity(8)
        if (target is HumanEntity && target.checkTargetIfPlayer()) {
            val inventory = target.inventory
            val content = mutableListOf(*inventory.contents)
            val shuffledContent = content.subList(0, 36)
            val rest = content.subList(36, content.size)
            shuffledContent.shuffle()
            shuffledContent.addAll(rest)
            inventory.contents = shuffledContent.toTypedArray()
            return true
        }
        return false
    }
}
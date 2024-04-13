package io.github.chaosdave34.kitpvp.items.impl.spacesoldier

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class SpaceSword : CustomItem(Material.DIAMOND_SWORD, "space_sword") {

    override fun getName(): Component = createSimpleItemName("Space Sword")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.DARKNESS)
}
package io.github.chaosdave34.kitpvp.items.impl.spacesoldier

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class BlackHoleGenerator: CustomItem(Material.SNOWBALL, "black_hole_generator", false, true) {

    override fun getName(): Component = createSimpleItemName("Black Hole Generator")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.BLACK_HOLE)
}
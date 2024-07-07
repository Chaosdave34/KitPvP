package io.github.chaosdave34.kitpvp.items.impl.spacesoldier

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material

class BlackHoleGenerator: CustomItem(Material.SNOWBALL, "black_hole_generator","Black Hole Generator" ,false, true) {
    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.BLACK_HOLE)
}
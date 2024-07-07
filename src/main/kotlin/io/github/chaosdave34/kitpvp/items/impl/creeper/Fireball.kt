package io.github.chaosdave34.kitpvp.items.impl.creeper

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material

class Fireball : CustomItem(Material.FIRE_CHARGE, "fireball", "Fireball", false, true) {
    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.FIREBALL)
}

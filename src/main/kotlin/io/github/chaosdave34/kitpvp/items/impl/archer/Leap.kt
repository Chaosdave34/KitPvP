package io.github.chaosdave34.kitpvp.items.impl.archer

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material

class Leap : CustomItem(Material.FEATHER, "leap", "Leap", stackable = false) {
    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.LEAP)
}
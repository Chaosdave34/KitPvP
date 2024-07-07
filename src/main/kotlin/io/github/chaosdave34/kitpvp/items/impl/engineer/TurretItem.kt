package io.github.chaosdave34.kitpvp.items.impl.engineer

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material

class TurretItem : CustomItem(Material.FIREWORK_ROCKET, "turret","Turret",  preventPlacingAndUsing = true) {
    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.TURRET)
}
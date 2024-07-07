package io.github.chaosdave34.kitpvp.items.impl.enderman

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material

class DragonsCharge : CustomItem(Material.FIRE_CHARGE, "dragons_charge","Dragon's Charge", false, true) {
    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.DRAGON_FIREBALL)
}
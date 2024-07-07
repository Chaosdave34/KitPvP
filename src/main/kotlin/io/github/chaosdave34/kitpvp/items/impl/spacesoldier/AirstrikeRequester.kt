package io.github.chaosdave34.kitpvp.items.impl.spacesoldier

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material

class AirstrikeRequester : CustomItem(Material.FIREWORK_ROCKET, "airstrike_requester", "Air Strike Requester",false, true) {
    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.AIRSTRIKE)
}
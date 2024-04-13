package io.github.chaosdave34.kitpvp.items.impl.spacesoldier

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class AirstrikeRequester : CustomItem(Material.FIREWORK_ROCKET, "airstrike_requester", false, true) {

    override fun getName(): Component = createSimpleItemName("Airstrike Requester")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.AIRSTRIKE)
}
package io.github.chaosdave34.kitpvp.items.impl.zeus

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material

class LightningWand : CustomItem(Material.STICK, "lightning_wand","Lightning Wand", false) {
    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.LIGHTNING, AbilityHandler.THUNDERSTORM)
}
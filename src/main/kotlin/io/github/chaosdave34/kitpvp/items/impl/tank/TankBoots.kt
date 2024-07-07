package io.github.chaosdave34.kitpvp.items.impl.tank

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material

class TankBoots : CustomItem(Material.IRON_BOOTS, "tank_boots", "Tank Boots") {
    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.GROUND_SLAM)
}
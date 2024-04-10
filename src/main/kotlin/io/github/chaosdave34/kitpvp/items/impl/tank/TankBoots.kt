package io.github.chaosdave34.kitpvp.items.impl.tank

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class TankBoots : CustomItem(Material.DIAMOND_BOOTS, "tank_boots") {

    override fun getName(): Component = createSimpleItemName("Tank Boots")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.GROUND_SLAM)
}
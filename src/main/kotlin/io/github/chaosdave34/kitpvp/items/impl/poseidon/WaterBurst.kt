package io.github.chaosdave34.kitpvp.items.impl.poseidon

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class WaterBurst : CustomItem(Material.SNOWBALL, "water_burst", false, true) {
    override fun getName(): Component = createSimpleItemName("Water Burst")

    override fun getAbilities(): List<Ability> {
        return listOf(AbilityHandler.WATER_BURST)
    }
}
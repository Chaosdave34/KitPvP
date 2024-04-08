package io.github.chaosdave34.kitpvp.items.impl.engineer

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class TurretItem : CustomItem(Material.FIREWORK_ROCKET, "turret", preventPlacingAndUsing = true) {

    override fun getName(): Component = createSimpleItemName("Turret")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.TURRET)
}
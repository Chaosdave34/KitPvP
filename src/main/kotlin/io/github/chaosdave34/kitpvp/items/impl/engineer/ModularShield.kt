package io.github.chaosdave34.kitpvp.items.impl.engineer

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class ModularShield : CustomItem(Material.BOOK, "modular_shield") {
    override fun getName(): Component = createSimpleItemName("Modular Shield")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.MODULAR_SHIELD)
}
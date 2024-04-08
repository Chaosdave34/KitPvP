package io.github.chaosdave34.kitpvp.items.impl.zeus

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class LightningWand : CustomItem(Material.STICK, "lightning_wand", false) {
    override fun getName(): Component = createSimpleItemName("Lightning Wand")

    override fun getDescription(): List<Component> = createSimpleDescription("Stolen from Zeus.")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.LIGHTNING, AbilityHandler.THUNDERSTORM)
}
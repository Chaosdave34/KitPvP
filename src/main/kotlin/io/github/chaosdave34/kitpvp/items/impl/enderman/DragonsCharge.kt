package io.github.chaosdave34.kitpvp.items.impl.enderman

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class DragonsCharge : CustomItem(Material.FIRE_CHARGE, "dragons_charge", false, true) {
    override fun getName(): Component = createSimpleItemName("Dragon's Charge")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.DRAGON_FIREBALL)
}
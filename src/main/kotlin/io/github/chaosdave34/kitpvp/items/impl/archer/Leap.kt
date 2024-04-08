package io.github.chaosdave34.kitpvp.items.impl.archer

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Leap : CustomItem(Material.FEATHER, "leap") {
    override fun getName(): Component {
        return createSimpleItemName("Leap")
    }

    override fun getAbilities(): List<Ability> {
        return listOf(AbilityHandler.LEAP)
    }
}
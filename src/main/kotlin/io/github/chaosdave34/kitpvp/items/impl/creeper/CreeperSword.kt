package io.github.chaosdave34.kitpvp.items.impl.creeper

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material

class CreeperSword: CustomItem(Material.IRON_SWORD, "creeper_sword") {
    override fun getName(): Component = createSimpleItemName("Creeper Sword")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.CHARGE)
}
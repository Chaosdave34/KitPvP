package io.github.chaosdave34.kitpvp.items.impl.creeper

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Fireball : CustomItem(Material.FIRE_CHARGE, "fireball", false,false) {

    override fun getName(): Component = createSimpleItemName("Fireball")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.FIREBALL)

    override fun additionalModifications(itemStack: ItemStack) {
        setCustomModelData(itemStack, 2)
    }
}

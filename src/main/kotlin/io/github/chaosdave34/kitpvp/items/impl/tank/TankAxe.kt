package io.github.chaosdave34.kitpvp.items.impl.tank

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class TankAxe : CustomItem(Material.IRON_AXE, "tank_axe") {

    override fun getName(): Component = createSimpleItemName("Tank Axe")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.TORNADO)

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.setCustomModelData(1)
    }
}
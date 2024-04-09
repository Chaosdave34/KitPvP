package io.github.chaosdave34.kitpvp.items.impl.artilleryman

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class RocketLauncher: CustomItem(Material.CROSSBOW, "rocket_launcher") {
    override fun getName(): Component = createSimpleItemName("Rocket Launcher")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.ENHANCE)

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.MULTISHOT, 1)
        itemStack.addEnchantment(Enchantment.QUICK_CHARGE, 3)
        itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1)

        itemStack.setCustomModelData(1)
    }
}
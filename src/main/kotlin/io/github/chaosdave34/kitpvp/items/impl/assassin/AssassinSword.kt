package io.github.chaosdave34.kitpvp.items.impl.assassin

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.enchantments.CustomEnchantments
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class AssassinSword : CustomItem(Material.IRON_SWORD, "assassin_sword") {

    override fun getName(): Component = createSimpleItemName("Assassin Sword")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.HAUNT)

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.setCustomModelData(3)
        itemStack.addEnchantment(CustomEnchantments.BACKSTAB, 2)
    }
}
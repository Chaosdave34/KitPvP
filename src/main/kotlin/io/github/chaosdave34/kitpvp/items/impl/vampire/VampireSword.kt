package io.github.chaosdave34.kitpvp.items.impl.vampire

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.enchantments.CustomEnchantments
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VampireSword : CustomItem(Material.DIAMOND_SWORD, "vampire_sword") {

    override fun getName(): Component = createSimpleItemName("Vampire Sword")

    override fun getAbilities(): List<Ability> {
        return listOf(AbilityHandler.BAT_MORPH)
    }

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(CustomEnchantments.LIFE_STEAL, 2)
        itemStack.setCustomModelData(1)
    }
}
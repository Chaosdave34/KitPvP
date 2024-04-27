package io.github.chaosdave34.kitpvp.items.impl.engineer

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class EngineersSword : CustomItem(Material.STONE_SWORD, "engineers_sword") {
    override fun getName(): Component = createSimpleItemName("Engineer's Sword")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.OVERLOAD)

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.SHARPNESS, 2)
    }
}
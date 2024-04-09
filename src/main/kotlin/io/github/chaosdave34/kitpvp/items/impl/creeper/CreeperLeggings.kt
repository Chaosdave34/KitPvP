package io.github.chaosdave34.kitpvp.items.impl.creeper

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class CreeperLeggings : CustomItem(Material.LEATHER_LEGGINGS, "creeper_leggings") {

    override fun getName(): Component = createSimpleItemName("Creeper Leggings")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.EXPLODE)

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4)

        itemStack.setLeatherArmorColor(Color.LIME)
    }
}
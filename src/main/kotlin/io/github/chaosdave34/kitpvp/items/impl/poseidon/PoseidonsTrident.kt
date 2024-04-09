package io.github.chaosdave34.kitpvp.items.impl.poseidon

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class PoseidonsTrident : CustomItem(Material.TRIDENT, "poseidons_trident") {

    override fun getName(): Component = createSimpleItemName("Poseidon's Trident")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.STORM)

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addUnsafeEnchantment(Enchantment.LOYALTY, 10)
        itemStack.addUnsafeEnchantment(Enchantment.CHANNELING, 5)
    }
}
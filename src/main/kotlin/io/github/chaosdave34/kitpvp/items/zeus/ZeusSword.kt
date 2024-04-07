package io.github.chaosdave34.kitpvp.items.zeus

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class ZeusSword : CustomItem(Material.GOLDEN_SWORD, "zeus_sword") {

    override fun getName(): Component = createSimpleItemName("Zeus Sword")

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 2);
    }

    override fun getAbilities(): List<Ability> {
        return listOf(AbilityHandler.RAGE)
    }
}
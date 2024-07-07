package io.github.chaosdave34.kitpvp.items.impl.artilleryman

import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class RocketLauncher : CustomItem(Material.CROSSBOW, "rocket_launcher", "Rocket Launcher") {
    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.MULTISHOT, 1)
        itemStack.addEnchantment(Enchantment.QUICK_CHARGE, 1)
        itemStack.addUnsafeEnchantment(Enchantment.INFINITY, 1)

        itemStack.setCustomModelData(1)
    }
}
package io.github.chaosdave34.kitpvp.elytrakits.impl

import io.github.chaosdave34.kitpvp.elytrakits.ElytraKit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class RocketLauncherKit : ElytraKit("elytra_rocket_launcher", "Rocket Launcher", Material.CROSSBOW) {

    override fun getInventoryContent(): Array<ItemStack?> {
        val crossbow = ItemStack.of(Material.CROSSBOW)
        crossbow.addUnsafeEnchantment(Enchantment.INFINITY, 1)
        crossbow.addEnchantment(Enchantment.QUICK_CHARGE, 3)

        return arrayOf(
            crossbow,
        )
    }
}
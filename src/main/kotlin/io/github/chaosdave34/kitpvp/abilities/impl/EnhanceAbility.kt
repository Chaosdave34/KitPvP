package io.github.chaosdave34.kitpvp.abilities.impl

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class EnhanceAbility : Ability("enhance", "Enhance", 30, 50, Material.SUGAR) {

    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Instantly load your rocket launcher for 3 seconds.")

    override fun onAbility(player: Player): Boolean {
        val crossbow = player.inventory.itemInMainHand
        crossbow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 5)

        AbilityRunnable.runTaskLater(KitPvp.INSTANCE, { crossbow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 3) }, player, 3 * 10)

        return true
    }
}
package io.github.chaosdave34.kitpvp.abilities.impl.zeus

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import net.kyori.adventure.text.Component
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class RageAbility : Ability("rage", "Rage", Type.RIGHT_CLICK, 20) {
    override fun getDescription(): List<Component> = createSimpleDescription("Gain thorns 10 for 10s.")

    override fun onAbility(player: Player): Boolean {
        player.isGlowing = true

        for (armorContent in player.inventory.armorContents) {
            armorContent?.addUnsafeEnchantment(Enchantment.THORNS, 10)
        }

        object : AbilityRunnable(player) {
            override fun runInGame() {
                player.isGlowing = false
                for (armorContent in player.inventory.armorContents) {
                    armorContent?.removeEnchantment(Enchantment.THORNS)
                }
            }
        }.runTaskLater(KitPvp.INSTANCE, 10 * 20)

        return true
    }
}
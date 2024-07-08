package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class RageUltimate: Ultimate("rage", "Rage", 10.0, Material.RAVAGER_SPAWN_EGG) {
    override fun getDescription() = createSimpleDescriptionAsList("Gain thorns 10 for 10s.")

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
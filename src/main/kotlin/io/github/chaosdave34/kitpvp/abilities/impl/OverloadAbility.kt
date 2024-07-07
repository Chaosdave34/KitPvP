package io.github.chaosdave34.kitpvp.abilities.impl

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class OverloadAbility : Ability("overload", "Overload", Type.RIGHT_CLICK, 25) {
    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Reduces your health by 50% but gain strength for 8s.")

    override fun onAbility(player: Player): Boolean {
        player.world.playSound(player.location, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.PLAYERS, 1f, 1.8f)

        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return false

        val currentHealthPercentage = player.health / maxHealth.value

        maxHealth.baseValue = ExtendedPlayer.from(player).getSelectedKitsKit().getMaxHealth() / 2

        player.health = maxHealth.value * currentHealthPercentage

        player.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 8 * 20, 1))

        player.isGlowing = true

        object : AbilityRunnable(player) {
            override fun runInGame() {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = ExtendedPlayer.from(player).getSelectedKitsKit().getMaxHealth()
                player.isGlowing = false
            }
        }.runTaskLater(KitPvp.INSTANCE, 8 * 20)

        return true
    }
}
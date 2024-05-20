package io.github.chaosdave34.kitpvp.abilities.impl.enderman

import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EnderAttackAbility : Ability("ender_attack", "Ender Attack", Type.RIGHT_CLICK, 15) {
    override fun getDescription(): List<Component> =
        createSimpleDescriptionAsList("Teleports you behind the enemy you are looking at in a 50 block radius. Gain speed and strength for 5 seconds.")

    override fun onAbility(player: Player): Boolean {
        val target = player.getTargetEntity(50, true)
        if (target is LivingEntity && target.checkTargetIfPlayer()) {
            player.teleport(target.getLocation().subtract(target.getLocation().direction))

            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 5 * 20, 2))
            player.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 5 * 20, 0))

            return true
        }
        return false
    }
}
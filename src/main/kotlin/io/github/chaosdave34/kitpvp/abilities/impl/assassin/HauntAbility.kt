package io.github.chaosdave34.kitpvp.abilities.impl.assassin

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.ExtendedPlayer.Companion.from
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable.Companion.runTaskLater
import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class HauntAbility : Ability("haunt", "Haunt", Type.RIGHT_CLICK, 20) {
    override fun getDescription(): List<Component> {
        return createSimpleDescriptionAsList("Gives you a speed boost and makes you invisible for 5 seconds. Runs out when you hit a player.")
    }

    override fun onAbility(player: Player): Boolean {
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 5 * 10, 9))
        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 5 * 10, 0))
        player.addScoreboardTag("haunt_ability")

        player.inventory.helmet = ItemStack.of(Material.AIR)
        player.inventory.chestplate = ItemStack.of(Material.AIR)
        player.inventory.leggings = ItemStack.of(Material.AIR)
        player.inventory.boots = ItemStack.of(Material.AIR)

        runTaskLater(KitPvp.INSTANCE, { removeEffects(player) }, player, (5 * 20).toLong())
        return true
    }

    @EventHandler
    fun onPlayerAttack(event: EntityDealDamageEvent) {
        val damager = event.damager
        if (damager is Player) {
            val extendedPlayer: ExtendedPlayer = from(damager)
            if (extendedPlayer.gameState == ExtendedPlayer.GameState.KITS_IN_GAME && damager.getScoreboardTags().contains("haunt_ability")) {
                removeEffects(damager)
            }
        }
    }

    private fun removeEffects(player: Player) {
        player.removePotionEffect(PotionEffectType.SPEED)
        player.removePotionEffect(PotionEffectType.INVISIBILITY)

        player.removeScoreboardTag("haunt_ability")

        val kit = from(player).getSelectedKitsKit()
        player.inventory.helmet = kit.getHeadContent()
        player.inventory.chestplate = kit.getChestContent()
        player.inventory.leggings = kit.getLegsContent()
        player.inventory.boots = kit.getFeetContent()
    }
}
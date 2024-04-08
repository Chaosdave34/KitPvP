package io.github.chaosdave34.kitpvp.kits

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.companions.Companion
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import io.github.chaosdave34.kitpvp.utils.ItemUtilities

abstract class Kit(val id: String, val name: String) : Listener, ItemUtilities {

    open fun getMaxHealth(): Double = 20.0

    open fun getMovementSpeed(): Double = 0.1

    open fun getHeadContent(): ItemStack? = null

    open fun getChestContent(): ItemStack? = null

    open fun getLegsContent(): ItemStack? = null

    open fun getFeetContent(): ItemStack? = null

    open fun getOffhandContent(): ItemStack? = null

    open fun getInventoryContent(): Array<ItemStack?> = arrayOf()

    open fun getKillRewards(): Array<ItemStack> = arrayOf()

    open fun getPotionEffects(): Map<PotionEffectType, Int> = mapOf()

    open fun getCompanion(): Companion? = null

    open fun apply(player: Player) {
        val extendedPlayer = ExtendedPlayer.from(player)
        extendedPlayer.setSelectedKitId(id)

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = getMaxHealth()
        player.health = getMaxHealth()

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = getMovementSpeed()

        val inventory = player.inventory

        inventory.contents = getInventoryContent()
        inventory.helmet = getHeadContent()
        inventory.chestplate = getChestContent()
        inventory.leggings = getLegsContent()
        inventory.boots = getFeetContent()
        inventory.setItemInOffHand(getOffhandContent())
        inventory.addItem(*getKillRewards())

        player.clearActivePotionEffects()

        for ((effectType, amplifier) in getPotionEffects()) {
            player.addPotionEffect(PotionEffect(effectType, -1, amplifier, false, false, false))
        }

        extendedPlayer.removeCompanion()
        extendedPlayer.spawnCompanion()
    }

    open fun Player.isKitSelected(): Boolean = id == ExtendedPlayer.from(this).selectedKitsKitId

    open fun Player.isKitActive(): Boolean = isKitSelected() && ExtendedPlayer.from(this).gameState == ExtendedPlayer.GameState.KITS_IN_GAME
}
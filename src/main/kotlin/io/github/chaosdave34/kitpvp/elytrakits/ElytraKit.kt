package io.github.chaosdave34.kitpvp.elytrakits

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

abstract class ElytraKit(val id: String, val name: String, val icon: Material) : Listener {

    open fun getMaxHealth(): Double = 20.0

    open fun getMovementSpeed(): Double = 0.1

    open fun getHeadContent(): ItemStack? = null

    fun getChestContent(): ItemStack = ItemStack.of(Material.ELYTRA)

    open fun getLegsContent(): ItemStack? = null

    open fun getFeetContent(): ItemStack? = null

    fun getOffhandContent(): ItemStack {
        val firework = ItemStack.of(Material.FIREWORK_ROCKET, 3)
        firework.editMeta(FireworkMeta::class.java) { fireworkMeta -> fireworkMeta.power = 3 }
        return firework
    }

    open fun getInventoryContent(): Array<ItemStack?> = arrayOf()

    open fun getKillRewards(): Array<ItemStack> = arrayOf()

    open fun getPotionEffects() = emptyMap<PotionEffectType, Int>()

    fun apply(player: Player) {
        val extendedPlayer = ExtendedPlayer.from(player)
        extendedPlayer.setSelectedElytraKitId(id)

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

        inventory.setItem(17, ItemStack.of(Material.ARROW))

        player.clearActivePotionEffects()

        for ((effectType, amplifier) in getPotionEffects()) {
            player.addPotionEffect(PotionEffect(effectType, -1, amplifier, false, false, false))
        }

        extendedPlayer.removeCompanion()
    }

    fun Player.isKitSelected(): Boolean = id == ExtendedPlayer.from(this).selectedElytraKitId

    fun Player.isKitActive(): Boolean = isKitSelected() && ExtendedPlayer.from(this).gameState == ExtendedPlayer.GameState.ELYTRA_IN_GAME
}
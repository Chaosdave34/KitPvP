package io.github.chaosdave34.kitpvp.kits

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.potion.PotionEffect

abstract class ElytraKit(id: String, name: String, val icon: Material) : Kit(id, name) {

    override fun getChestContent(): ItemStack = ItemStack.of(Material.ELYTRA)

    override fun getOffhandContent(): ItemStack {
        val firework = ItemStack.of(Material.FIREWORK_ROCKET, 3)
        firework.editMeta(FireworkMeta::class.java) { fireworkMeta -> fireworkMeta.power = 3 }
        return firework
    }

    override fun apply(player: Player) {
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

    override fun Player.isKitSelected(): Boolean = id == ExtendedPlayer.from(this).selectedElytraKitId

    override fun Player.isKitActive(): Boolean = isKitSelected() && ExtendedPlayer.from(this).gameState == ExtendedPlayer.GameState.ELYTRA_IN_GAME
}
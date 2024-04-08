package io.github.chaosdave34.kitpvp.items.impl.archer

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

class LongBow : CustomItem(Material.BOW, "long_bow") {
    override fun getName(): Component = createSimpleItemName("Long Bow")

    override fun getDescription(): List<Component> = createSimpleDescription("Increased damage but takes a while to recharge.")

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.addEnchantment(Enchantment.ARROW_INFINITE, 1)
        itemStack.addEnchantment(Enchantment.ARROW_DAMAGE, 5)
        itemStack.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2)

        setCustomModelData(itemStack, 2)
    }

    @EventHandler
    fun onArrowShot(event: EntityShootBowEvent) {
        if (event.entity is Player) {
            val player = event.entity as Player
            val extendedPlayer = ExtendedPlayer.from(player)

            if (extendedPlayer.gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {
                val bow = event.bow ?: return
                if (bow.isThisCustomItem()) {
                    val itemMeta = bow.itemMeta as Damageable

                    if (itemMeta.hasDamage()) event.isCancelled = true
                    else {
                        itemMeta.damage = 384
                        bow.itemMeta = itemMeta

                        object : AbilityRunnable(extendedPlayer) {
                            var i = 0
                            override fun runInGame() {
                                bow.editMeta(Damageable::class.java) { damageable -> damageable.damage = 384 - (384.0 * (i / (10.0 * 20.0))).toInt() }

                                if (i == 10 * 20) cancel()

                                i++
                            }
                        }.runTaskTimer(KitPvp.INSTANCE, 0, 1)
                    }
                }
            }
        }
    }

    @EventHandler
    fun chargeBow(event: PlayerInteractEvent) {
        val player = event.player
        val extendedPlayer = ExtendedPlayer.from(player)

        if (extendedPlayer.gameState == ExtendedPlayer.GameState.KITS_IN_GAME) {
            val bow = event.item ?: return
            if (bow.isThisCustomItem()) {
                if ((bow.itemMeta as Damageable).hasDamage()) {
                    event.isCancelled = true
                }
            }
        }
    }
}
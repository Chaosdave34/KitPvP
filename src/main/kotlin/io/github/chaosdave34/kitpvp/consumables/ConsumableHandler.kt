package io.github.chaosdave34.kitpvp.consumables

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType
import kotlin.math.min

class ConsumableHandler {
    val consumables: MutableMap<String, Consumable> = mutableMapOf()

    init {
        val healingPotions = Consumable("healing_potion", "Healing Potion", 3, 1, 5) {
            val item = ItemStack.of(Material.SPLASH_POTION)
            item.editMeta(PotionMeta::class.java) {
                it.basePotionType = PotionType.HEALING
            }
            item
        }
        registerConsumable(healingPotions)

        val totemOfUndying = Consumable("totem_of_undying", "Totem of Undying", 1, 1, 1) {
            ItemStack.of(Material.TOTEM_OF_UNDYING)
        }
        registerConsumable(totemOfUndying)

        val goldenApple = Consumable("golden_apple", "Golden Apple", 2, 1, 4) {
            ItemStack.of(Material.GOLDEN_APPLE)
        }
        registerConsumable(goldenApple)
    }

    fun rewardOnKill(player: Player) {
        val extendedPlayer = ExtendedPlayer.from(player)
        val inventory = player.inventory

        val consumable1 = extendedPlayer.selectedSetup.getConsumable1()
        val consumable2 = extendedPlayer.selectedSetup.getConsumable2()

        if (consumable1 != null) {
            val currentAmount = inventory.getItem(7)?.amount ?: 0
            val newAmount = min(consumable1.maxAmount, currentAmount + consumable1.rewardAmount)

            inventory.getItem(7)?.amount = newAmount
        }

        if (consumable2 != null) {
            val currentAmount = inventory.getItem(8)?.amount ?: 0
            val newAmount = min(consumable2.maxAmount, currentAmount + consumable2.rewardAmount)

            inventory.getItem(8)?.amount = newAmount
        }
    }

    private fun registerConsumable(consumable: Consumable): Consumable {
        consumables[consumable.id] = consumable
        return consumable
    }
}
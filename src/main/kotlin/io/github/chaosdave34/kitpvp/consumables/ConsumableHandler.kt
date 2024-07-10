package io.github.chaosdave34.kitpvp.consumables

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionType

// Todo handle kill rewards
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

    private fun registerConsumable(consumable: Consumable): Consumable {
        consumables[consumable.id] = consumable
        return consumable
    }
}
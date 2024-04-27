package io.github.chaosdave34.kitpvp.enchantments

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.enchantments.impl.BackstabEnchantment
import io.github.chaosdave34.kitpvp.enchantments.impl.FreezeEnchantment
import io.github.chaosdave34.kitpvp.enchantments.impl.LifeStealEnchantment
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class EnchantmentHandler {
    private val enchantments: MutableMap<String, CustomEnchantment> = mutableMapOf()

    companion object {
        lateinit var BACKSTAB: CustomEnchantment
        lateinit var FREEZE: CustomEnchantment
        lateinit var LIFE_STEAL: CustomEnchantment

        private val enchantmentsKey = NamespacedKey(KitPvp.INSTANCE, "enchantments")

        fun containsEnchantment(itemStack: ItemStack, id: String): Boolean {
            val container = itemStack.itemMeta.persistentDataContainer

            val enchantmentContainer = container.get(enchantmentsKey, PersistentDataType.TAG_CONTAINER) ?: return false
            return enchantmentContainer.has(NamespacedKey(KitPvp.INSTANCE, id))
        }

        fun getEnchantmentLevel(itemStack: ItemStack, id: String): Int {
            val container = itemStack.itemMeta.persistentDataContainer

            val enchantmentContainer = container.get(enchantmentsKey, PersistentDataType.TAG_CONTAINER)
            return enchantmentContainer?.get(NamespacedKey(KitPvp.INSTANCE, id), PersistentDataType.INTEGER) ?: -1
        }
    }

    init {
        BACKSTAB = registerEnchantment(BackstabEnchantment())
        FREEZE = registerEnchantment(FreezeEnchantment())
        LIFE_STEAL = registerEnchantment(LifeStealEnchantment())
    }

    private fun registerEnchantment(customEnchantment: CustomEnchantment): CustomEnchantment {
        enchantments[customEnchantment.id] = customEnchantment
        Utils.registerEvents(customEnchantment)
        return customEnchantment
    }

    fun getEnchantment(id: String): CustomEnchantment? {
        return enchantments[id]
    }
}
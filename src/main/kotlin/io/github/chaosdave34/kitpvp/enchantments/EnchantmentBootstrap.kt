package io.github.chaosdave34.kitpvp.enchantments

import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.data.EnchantmentRegistryEntry
import io.papermc.paper.registry.event.RegistryFreezeEvent
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup

object EnchantmentBootstrap {
    fun register(event: RegistryFreezeEvent<Enchantment, EnchantmentRegistryEntry.Builder>) {
        val registry = event.registry()

        registry.register(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("kitpvp:backstab"))) {
            it.description(Component.text("Backstab"))
                .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.SWORDS))
                .anvilCost(1)
                .maxLevel(5)
                .weight(10)
                .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                .activeSlots(EquipmentSlotGroup.HAND)
        }

        registry.register(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("kitpvp:freeze"))) {
            it.description(Component.text("Freeze"))
                .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.SWORDS))
                .anvilCost(1)
                .maxLevel(5)
                .weight(10)
                .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                .activeSlots(EquipmentSlotGroup.HAND)
        }

        registry.register(TypedKey.create(RegistryKey.ENCHANTMENT, Key.key("kitpvp:life_steal"))) {
            it.description(Component.text("Life Steal"))
                .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.SWORDS))
                .anvilCost(1)
                .maxLevel(5)
                .weight(10)
                .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                .activeSlots(EquipmentSlotGroup.HAND)
        }
    }
}
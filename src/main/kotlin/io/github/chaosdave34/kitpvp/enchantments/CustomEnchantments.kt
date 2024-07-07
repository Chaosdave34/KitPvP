package io.github.chaosdave34.kitpvp.enchantments

import io.github.chaosdave34.kitpvp.KitPvp
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import org.bukkit.NamespacedKey

object CustomEnchantments {
    private val registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)

    val BACKSTAB = registry.getOrThrow(NamespacedKey(KitPvp.INSTANCE, "backstab"))
    val FREEZE = registry.getOrThrow(NamespacedKey(KitPvp.INSTANCE, "freeze"))
    val LIFE_STEAL = registry.getOrThrow(NamespacedKey(KitPvp.INSTANCE, "life_steal"))
}
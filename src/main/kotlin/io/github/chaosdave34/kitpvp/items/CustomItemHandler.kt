package io.github.chaosdave34.kitpvp.items

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.Utils
import io.github.chaosdave34.kitpvp.items.impl.*
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class CustomItemHandler {
    val customItems: MutableMap<String, CustomItem> = mutableMapOf()

    companion object {
        fun ItemStack.getCustomItemId(): String? = this.persistentDataContainer.get(NamespacedKey(KitPvp.INSTANCE, "id"), PersistentDataType.STRING)
    }

    init {
        registerItem(MagicWand())
        registerItem(VampireSword())
        registerItem(CreeperLeggings())
        registerItem(EnderSword())
        registerItem(DevilsSword())
        registerItem(TankBoots())
        registerItem(Jetpack())
        registerItem(AssassinSword())
        registerItem(RocketLauncher())
        registerItem(LongBow())
        registerItem(EngineersSword())

        registerItem(CustomItem(Material.IRON_HELMET, "iron_helmet", "Iron Helmet"))
        registerItem(CustomItem(Material.IRON_CHESTPLATE, "iron_chestplate", "Iron Chestplate"))
        registerItem(CustomItem(Material.IRON_LEGGINGS, "iron_leggings", "Iron Leggings"))
        registerItem(CustomItem(Material.IRON_BOOTS, "iron_boots", "Iron Boots"))
    }

    private fun registerItem(item: CustomItem): CustomItem {
        Utils.registerEvents(item)
        customItems[item.id] = item
        return item
    }
}
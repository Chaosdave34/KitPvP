package io.github.chaosdave34.kitpvp.items

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.Utils
import io.github.chaosdave34.kitpvp.items.impl.*
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class CustomItemHandler {
    val customItems: MutableMap<String, CustomItem> = mutableMapOf()

    companion object {
        lateinit var MAGIC_WAND: CustomItem
        lateinit var VAMPIRE_SWORD: CustomItem
        lateinit var CREEPER_LEGGINGS: CustomItem
        lateinit var ENDER_SWORD: CustomItem
        lateinit var DEVILS_SWORD: CustomItem
        lateinit var TANK_BOOTS: CustomItem
        lateinit var JETPACK: CustomItem
        lateinit var ASSASSIN_SWORD: CustomItem
        lateinit var ROCKET_LAUNCHER: CustomItem
        lateinit var LONG_BOW: CustomItem
        lateinit var ENGINEERS_SWORD: CustomItem

        fun ItemStack.getCustomItemId(): String? = this.persistentDataContainer.get(NamespacedKey(KitPvp.INSTANCE, "id"), PersistentDataType.STRING)
    }

    init {
        MAGIC_WAND = registerItem(MagicWand())
        VAMPIRE_SWORD = registerItem(VampireSword())
        CREEPER_LEGGINGS = registerItem(CreeperLeggings())
        ENDER_SWORD = registerItem(EnderSword())
        DEVILS_SWORD = registerItem(DevilsSword())
        TANK_BOOTS = registerItem(TankBoots())
        JETPACK = registerItem(Jetpack())
        ASSASSIN_SWORD = registerItem(AssassinSword())
        ROCKET_LAUNCHER = registerItem(RocketLauncher())
        LONG_BOW = registerItem(LongBow())
        ENGINEERS_SWORD = registerItem(EngineersSword())
    }

    private fun registerItem(item: CustomItem): CustomItem {
        Utils.registerEvents(item)
        customItems[item.id] = item
        return item
    }
}
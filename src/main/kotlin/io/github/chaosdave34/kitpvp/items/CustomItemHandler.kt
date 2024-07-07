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
        lateinit var FIREBALL: CustomItem
        lateinit var LIGHTNING_WAND: CustomItem
        lateinit var MAGIC_WAND: CustomItem
        lateinit var VAMPIRE_SWORD: CustomItem
        lateinit var AIRSTRIKE_REQUESTER: CustomItem
        lateinit var CREEPER_LEGGINGS: CustomItem
        lateinit var ENDER_SWORD: CustomItem
        lateinit var DEVILS_SWORD: CustomItem
        lateinit var TANK_BOOTS: CustomItem
        lateinit var JETPACK: CustomItem
        lateinit var ASSASSIN_SWORD: CustomItem
        lateinit var ROCKET_LAUNCHER: CustomItem
        lateinit var TURRET: CustomItem
        lateinit var LEAP: CustomItem
        lateinit var DRAGONS_CHARGE: CustomItem
        lateinit var LONG_BOW: CustomItem
        lateinit var ENGINEERS_SWORD: CustomItem
        lateinit var WATER_BURST: CustomItem
        lateinit var BLACK_HOLE_GENERATOR: CustomItem

        fun ItemStack.getCustomItemId(): String? = this.persistentDataContainer.get(NamespacedKey(KitPvp.INSTANCE, "id"), PersistentDataType.STRING)
    }

    init {
        FIREBALL = registerItem(Fireball())
        LIGHTNING_WAND = registerItem(LightningWand())
        MAGIC_WAND = registerItem(MagicWand())
        VAMPIRE_SWORD = registerItem(VampireSword())
        AIRSTRIKE_REQUESTER = registerItem(AirstrikeRequester())
        CREEPER_LEGGINGS = registerItem(CreeperLeggings())
        ENDER_SWORD = registerItem(EnderSword())
        DEVILS_SWORD = registerItem(DevilsSword())
        TANK_BOOTS = registerItem(TankBoots())
        JETPACK = registerItem(Jetpack())
        ASSASSIN_SWORD = registerItem(AssassinSword())
        ROCKET_LAUNCHER = registerItem(RocketLauncher())
        TURRET = registerItem(TurretItem())
        LEAP = registerItem(Leap())
        DRAGONS_CHARGE = registerItem(DragonsCharge())
        LONG_BOW = registerItem(LongBow())
        ENGINEERS_SWORD = registerItem(EngineersSword())
        WATER_BURST = registerItem(WaterBurst())
        BLACK_HOLE_GENERATOR = registerItem(BlackHoleGenerator())
    }

    private fun registerItem(item: CustomItem): CustomItem {
        Utils.registerEvents(item)
        customItems[item.id] = item
        return item
    }
}
package io.github.chaosdave34.kitpvp.items

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.ghutils.utils.PDCUtils
import io.github.chaosdave34.kitpvp.items.impl.archer.Leap
import io.github.chaosdave34.kitpvp.items.impl.archer.LongBow
import io.github.chaosdave34.kitpvp.items.impl.artilleryman.Jetpack
import io.github.chaosdave34.kitpvp.items.impl.artilleryman.RocketLauncher
import io.github.chaosdave34.kitpvp.items.impl.assassin.AssassinSword
import io.github.chaosdave34.kitpvp.items.impl.creeper.CreeperLeggings
import io.github.chaosdave34.kitpvp.items.impl.creeper.CreeperSword
import io.github.chaosdave34.kitpvp.items.impl.creeper.Fireball
import io.github.chaosdave34.kitpvp.items.impl.devil.DevilsSword
import io.github.chaosdave34.kitpvp.items.impl.enderman.DragonsCharge
import io.github.chaosdave34.kitpvp.items.impl.enderman.EnderSword
import io.github.chaosdave34.kitpvp.items.impl.engineer.EngineersSword
import io.github.chaosdave34.kitpvp.items.impl.engineer.ModularShield
import io.github.chaosdave34.kitpvp.items.impl.engineer.TurretItem
import io.github.chaosdave34.kitpvp.items.impl.magician.MagicWand
import io.github.chaosdave34.kitpvp.items.impl.poseidon.PoseidonsTrident
import io.github.chaosdave34.kitpvp.items.impl.poseidon.WaterBurst
import io.github.chaosdave34.kitpvp.items.impl.spacesoldier.AirstrikeRequester
import io.github.chaosdave34.kitpvp.items.impl.spacesoldier.BlackHoleGenerator
import io.github.chaosdave34.kitpvp.items.impl.spacesoldier.SpaceSword
import io.github.chaosdave34.kitpvp.items.impl.tank.TankAxe
import io.github.chaosdave34.kitpvp.items.impl.tank.TankBoots
import io.github.chaosdave34.kitpvp.items.impl.vampire.VampireSword
import io.github.chaosdave34.kitpvp.items.impl.zeus.LightningWand
import io.github.chaosdave34.kitpvp.items.impl.zeus.ZeusSword
import org.bukkit.inventory.ItemStack

class CustomItemHandler {
    val customItems: MutableMap<String, CustomItem> = mutableMapOf()

    companion object {
        lateinit var FIREBALL: CustomItem
        lateinit var LIGHTNING_WAND: CustomItem
        lateinit var MAGIC_WAND: CustomItem
        lateinit var VAMPIRE_SWORD: CustomItem
        lateinit var AIRSTRIKE_REQUESTER: CustomItem
        lateinit var SPACE_SWORD: CustomItem
        lateinit var CREEPER_LEGGINGS: CustomItem
        lateinit var ENDER_SWORD: CustomItem
        lateinit var POSEIDONS_TRIDENT: CustomItem
        lateinit var DEVILS_SWORD: CustomItem
        lateinit var TANK_BOOTS: CustomItem
        lateinit var TANK_AXE: CustomItem
        lateinit var JETPACK: CustomItem
        lateinit var ASSASSIN_SWORD: CustomItem
        lateinit var ROCKET_LAUNCHER: CustomItem
        lateinit var TURRET: CustomItem
        lateinit var LEAP: CustomItem
        lateinit var DRAGONS_CHARGE: CustomItem
        lateinit var ZEUS_SWORD: CustomItem
        lateinit var LONG_BOW: CustomItem
        lateinit var MODULAR_SHIELD: CustomItem
        lateinit var ENGINEERS_SWORD: CustomItem
        lateinit var CREEPER_SWORD: CustomItem
        lateinit var WATER_BURST: CustomItem
        lateinit var BLACK_HOLE_GENERATOR: CustomItem

        fun getCustomItemId(itemStack: ItemStack): String = PDCUtils.getId(itemStack.itemMeta) ?: ""
    }

    init {
        FIREBALL = registerItem(Fireball())
        LIGHTNING_WAND = registerItem(LightningWand())
        MAGIC_WAND = registerItem(MagicWand())
        VAMPIRE_SWORD = registerItem(VampireSword())
        AIRSTRIKE_REQUESTER = registerItem(AirstrikeRequester())
        SPACE_SWORD = registerItem(SpaceSword())
        CREEPER_LEGGINGS = registerItem(CreeperLeggings())
        ENDER_SWORD = registerItem(EnderSword())
        POSEIDONS_TRIDENT = registerItem(PoseidonsTrident())
        DEVILS_SWORD = registerItem(DevilsSword())
        TANK_BOOTS = registerItem(TankBoots())
        TANK_AXE = registerItem(TankAxe())
        JETPACK = registerItem(Jetpack())
        ASSASSIN_SWORD = registerItem(AssassinSword())
        ROCKET_LAUNCHER = registerItem(RocketLauncher())
        TURRET = registerItem(TurretItem())
        LEAP = registerItem(Leap())
        DRAGONS_CHARGE = registerItem(DragonsCharge())
        ZEUS_SWORD = registerItem(ZeusSword())
        LONG_BOW = registerItem(LongBow())
        MODULAR_SHIELD = registerItem(ModularShield())
        ENGINEERS_SWORD = registerItem(EngineersSword())
        CREEPER_SWORD = registerItem(CreeperSword())
        WATER_BURST = registerItem(WaterBurst())
        BLACK_HOLE_GENERATOR = registerItem(BlackHoleGenerator())
    }

    private fun registerItem(item: CustomItem): CustomItem {
        Utils.registerEvents(item)
        customItems[item.id] = item
        return item
    }
}
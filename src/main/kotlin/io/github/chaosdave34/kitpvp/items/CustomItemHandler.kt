package io.github.chaosdave34.kitpvp.items

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.ghutils.utils.PDCUtils
import io.github.chaosdave34.kitpvp.items.impl.archer.Leap
import io.github.chaosdave34.kitpvp.items.impl.archer.LongBow
import io.github.chaosdave34.kitpvp.items.impl.artilleryman.Jetpack
import io.github.chaosdave34.kitpvp.items.impl.artilleryman.RocketLauncher
import io.github.chaosdave34.kitpvp.items.impl.assassin.AssassinSword
import io.github.chaosdave34.kitpvp.items.impl.creeper.CreeperLeggings
import io.github.chaosdave34.kitpvp.items.impl.creeper.FireballSword
import io.github.chaosdave34.kitpvp.items.impl.devil.DevilsSword
import io.github.chaosdave34.kitpvp.items.impl.enderman.DragonsCharge
import io.github.chaosdave34.kitpvp.items.impl.enderman.EnderSword
import io.github.chaosdave34.kitpvp.items.impl.engineer.EngineersSword
import io.github.chaosdave34.kitpvp.items.impl.engineer.ModularShield
import io.github.chaosdave34.kitpvp.items.impl.engineer.TurretItem
import io.github.chaosdave34.kitpvp.items.impl.magician.MagicWand
import io.github.chaosdave34.kitpvp.items.impl.poseidon.PoseidonsTrident
import io.github.chaosdave34.kitpvp.items.impl.provoker.NukeItem
import io.github.chaosdave34.kitpvp.items.impl.provoker.SpookSword
import io.github.chaosdave34.kitpvp.items.impl.tank.TankAxe
import io.github.chaosdave34.kitpvp.items.impl.tank.TankBoots
import io.github.chaosdave34.kitpvp.items.impl.vampire.VampireSword
import io.github.chaosdave34.kitpvp.items.impl.zeus.LightningWand
import io.github.chaosdave34.kitpvp.items.impl.zeus.ZeusSword
import org.bukkit.inventory.ItemStack

class CustomItemHandler {
    val customItems: MutableMap<String, CustomItem> = mutableMapOf()

    companion object {
        @JvmStatic
        lateinit var FIREBALL_SWORD: CustomItem

        @JvmStatic
        lateinit var LIGHTNING_WAND: CustomItem

        @JvmStatic
        lateinit var MAGIC_WAND: CustomItem

        @JvmStatic
        lateinit var VAMPIRE_SWORD: CustomItem

        @JvmStatic
        lateinit var NUKE: CustomItem

        @JvmStatic
        lateinit var SPOOK_SWORD: CustomItem

        @JvmStatic
        lateinit var CREEPER_LEGGINGS: CustomItem

        @JvmStatic
        lateinit var ENDER_SWORD: CustomItem

        @JvmStatic
        lateinit var POSEIDONS_TRIDENT: CustomItem

        @JvmStatic
        lateinit var DEVILS_SWORD: CustomItem

        @JvmStatic
        lateinit var TANK_BOOTS: CustomItem

        @JvmStatic
        lateinit var TANK_AXE: CustomItem

        @JvmStatic
        lateinit var JETPACK: CustomItem

        @JvmStatic
        lateinit var ASSASSIN_SWORD: CustomItem

        @JvmStatic
        lateinit var ROCKET_LAUNCHER: CustomItem

        @JvmStatic
        lateinit var TURRET: CustomItem

        @JvmStatic
        lateinit var LEAP: CustomItem

        @JvmStatic
        lateinit var DRAGONS_CHARGE: CustomItem

        @JvmStatic
        lateinit var ZEUS_SWORD: CustomItem

        @JvmStatic
        lateinit var LONG_BOW: CustomItem

        @JvmStatic
        lateinit var MODULAR_SHIELD: CustomItem

        @JvmStatic
        lateinit var ENGINEERS_SWORD: CustomItem

        @JvmStatic
        fun getCustomItemId(itemStack: ItemStack): String = PDCUtils.getId(itemStack.itemMeta) ?: ""
    }

    init {
        FIREBALL_SWORD = registerItem(FireballSword())
        LIGHTNING_WAND = registerItem(LightningWand())
        MAGIC_WAND = registerItem(MagicWand())
        VAMPIRE_SWORD = registerItem(VampireSword())
        NUKE = registerItem(NukeItem())
        SPOOK_SWORD = registerItem(SpookSword())
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
    }

    private fun registerItem(item: CustomItem): CustomItem {
        Utils.registerEvents(item)
        customItems[item.id] = item
        return item
    }
}
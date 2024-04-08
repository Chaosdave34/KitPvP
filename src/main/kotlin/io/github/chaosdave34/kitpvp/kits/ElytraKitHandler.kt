package io.github.chaosdave34.kitpvp.kits

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.kitpvp.kits.impl.elytra.*

class ElytraKitHandler {
    val kits: MutableMap<String, ElytraKit> = mutableMapOf()
    
    companion object {
        @JvmStatic
        lateinit var KNIGHT: ElytraKit
        @JvmStatic
        lateinit var SNIPER: ElytraKit
        @JvmStatic
        lateinit var PYRO: ElytraKit
        @JvmStatic
        lateinit var TANK: ElytraKit
        @JvmStatic
        lateinit var KNOCKER: ElytraKit
        @JvmStatic
        lateinit var ROCKET_LAUNCHER: ElytraKit
        @JvmStatic
        lateinit var POSEIDON: ElytraKit
        @JvmStatic
        lateinit var TELEPORTER: ElytraKit
        @JvmStatic
        lateinit var HEALER: ElytraKit
        @JvmStatic
        lateinit var CHEMIST: ElytraKit
    }

    init {
        KNIGHT = registerKit(KnightKit())
        SNIPER = registerKit(SniperKit())
        PYRO = registerKit(PyroKit())
        TANK = registerKit(TankKit())
        KNOCKER = registerKit(KnockerKit())
        ROCKET_LAUNCHER = registerKit(RocketLauncherKit())
        POSEIDON = registerKit(PoseidonKit())
        TELEPORTER = registerKit(TeleporterKit())
        HEALER = registerKit(HealerKit())
        CHEMIST = registerKit(ChemistKit())
    }

    private fun registerKit(kit: ElytraKit): ElytraKit {
        kits[kit.id] = kit
        Utils.registerEvents(kit)
        return kit
    }
}
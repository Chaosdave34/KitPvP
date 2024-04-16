package io.github.chaosdave34.kitpvp.kits

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.kitpvp.kits.impl.elytra.*

class ElytraKitHandler {
    val kits: MutableMap<String, ElytraKit> = mutableMapOf()

    companion object {
        lateinit var KNIGHT: ElytraKit
        lateinit var SNIPER: ElytraKit
        lateinit var PYRO: ElytraKit
        lateinit var TANK: ElytraKit
        lateinit var KNOCKER: ElytraKit
        lateinit var ROCKET_LAUNCHER: ElytraKit
        lateinit var POSEIDON: ElytraKit
        lateinit var TELEPORTER: ElytraKit
        lateinit var HEALER: ElytraKit
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
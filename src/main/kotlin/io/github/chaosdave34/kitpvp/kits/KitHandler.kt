package io.github.chaosdave34.kitpvp.kits

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.kitpvp.kits.impl.kits.*

class KitHandler {
    val kits: MutableMap<String, Kit> = mutableMapOf()

    companion object {
        @JvmStatic
        lateinit var KNIGHT: Kit

        @JvmStatic
        lateinit var ZEUS: Kit

        @JvmStatic
        lateinit var TANK: Kit

        @JvmStatic
        lateinit var PROVOKER: Kit

        @JvmStatic
        lateinit var ARCHER: Kit

        @JvmStatic
        lateinit var ARTILLERYMAN: Kit

        @JvmStatic
        lateinit var ASSASSIN: Kit

        @JvmStatic
        lateinit var ENGINEER: Kit

        @JvmStatic
        lateinit var MAGICIAN: Kit

        @JvmStatic
        lateinit var VAMPIRE: Kit

        @JvmStatic
        lateinit var CREEPER: Kit

        @JvmStatic
        lateinit var ENDERMAN: Kit

        @JvmStatic
        lateinit var POSEIDON: Kit

        @JvmStatic
        lateinit var DEVIL: Kit
    }

    init {
        KNIGHT = registerKit(KnightKit())
        ZEUS = registerKit(ZeusKit())
        TANK = registerKit(TankKit())
        PROVOKER = registerKit(SpaceSoldierKit())
        ARCHER = registerKit(ArcherKit())
        ARTILLERYMAN = registerKit(ArtilleryManKit())
        ASSASSIN = registerKit(AssassinKit())
        ENGINEER = registerKit(EngineerKit())
        MAGICIAN = registerKit(MagicianKit())
        VAMPIRE = registerKit(VampireKit())
        CREEPER = registerKit(CreeperKit())
        ENDERMAN = registerKit(EndermanKit())
        POSEIDON = registerKit(PoseidonKit())
        DEVIL = registerKit(DevilKit())
    }

    private fun registerKit(kit: Kit): Kit {
        kits[kit.id] = kit
        Utils.registerEvents(kit)
        return kit
    }
}
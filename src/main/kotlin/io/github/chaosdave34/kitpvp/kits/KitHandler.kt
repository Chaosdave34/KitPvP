package io.github.chaosdave34.kitpvp.kits

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.kitpvp.kits.impl.kits.*

class KitHandler {
    val kits: MutableMap<String, Kit> = mutableMapOf()

    companion object {
        lateinit var KNIGHT: Kit
        lateinit var ZEUS: Kit
        lateinit var TANK: Kit
        lateinit var PROVOKER: Kit
        lateinit var ARCHER: Kit
        lateinit var ARTILLERYMAN: Kit
        lateinit var ASSASSIN: Kit
        lateinit var ENGINEER: Kit
        lateinit var MAGICIAN: Kit
        lateinit var VAMPIRE: Kit
        lateinit var CREEPER: Kit
        lateinit var ENDERMAN: Kit
        lateinit var POSEIDON: Kit
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
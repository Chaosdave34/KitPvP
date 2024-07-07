package io.github.chaosdave34.kitpvp.abilities

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.Utils
import io.github.chaosdave34.kitpvp.abilities.impl.*
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class AbilityHandler : Listener {
    val abilities: MutableMap<String, Ability> = mutableMapOf()

    companion object {
        lateinit var FIREBALL: Ability
        lateinit var LIGHTNING: Ability
        lateinit var THUNDERSTORM: Ability
        lateinit var HAUNT: Ability
        lateinit var SHUFFLE: Ability
        lateinit var BAT_MORPH: Ability
        lateinit var AIRSTRIKE: Ability
        lateinit var EXPLODE: Ability
        lateinit var ENDER_ATTACK: Ability
        lateinit var GROUND_SLAM: Ability
        lateinit var ENHANCE: Ability
        lateinit var TURRET: Ability
        lateinit var LEAP: Ability
        lateinit var DRAGON_FIREBALL: Ability
        lateinit var FIRE_STORM: Ability
        lateinit var OVERLOAD: Ability
        lateinit var WATER_BURST: Ability
        lateinit var BLACK_HOLE: Ability
    }

    init {
        FIREBALL = registerAbility(FireballAbility())
        LIGHTNING = registerAbility(LightningAbility())
        THUNDERSTORM = registerAbility(ThunderstormAbility())
        HAUNT = registerAbility(HauntAbility())
        SHUFFLE = registerAbility(ShuffleAbility())
        BAT_MORPH = registerAbility(BatMorphAbility())
        AIRSTRIKE = registerAbility(AirstrikeAbility())
        EXPLODE = registerAbility(ExplodeAbility())
        ENDER_ATTACK = registerAbility(EnderAttackAbility())
        GROUND_SLAM = registerAbility(GroundSlamAbility())
        ENHANCE = registerAbility(EnhanceAbility())
        TURRET = registerAbility(TurretAbility())
        LEAP = registerAbility(LeapAbility())
        DRAGON_FIREBALL = registerAbility(DragonFireballAbility())
        FIRE_STORM = registerAbility(FireStormAbility())
        OVERLOAD = registerAbility(OverloadAbility())
        WATER_BURST = registerAbility(WaterBurstAbility())
        BLACK_HOLE = registerAbility(BlackHoleAbility())
    }

    private fun registerAbility(ability: Ability): Ability {
        Utils.registerEvents(ability)
        abilities[ability.id] = ability
        return ability
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return

        if (ExtendedPlayer.from(player).inSpawn()) return

        val abilityId = item.persistentDataContainer.get(NamespacedKey(KitPvp.INSTANCE, "ability"), PersistentDataType.STRING)

        abilities[abilityId]?.handleAbility(player)
    }
}
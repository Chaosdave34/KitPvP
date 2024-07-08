package io.github.chaosdave34.kitpvp.abilities

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.Utils
import io.github.chaosdave34.kitpvp.abilities.impl.*
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class AbilityHandler : Listener {
    val abilities: MutableMap<String, Ability> = mutableMapOf()

    init {
        registerAbility(FireballAbility())
        registerAbility(LightningAbility())
        registerAbility(ThunderstormAbility())
        registerAbility(HauntAbility())
        registerAbility(ShuffleAbility())
        registerAbility(BatMorphAbility())
        registerAbility(AirstrikeAbility())
        registerAbility(ExplodeAbility())
        registerAbility(EnderAttackAbility())
        registerAbility(GroundSlamAbility())
        registerAbility(EnhanceAbility())
        registerAbility(TurretAbility())
        registerAbility(LeapAbility())
        registerAbility(DragonFireballAbility())
        registerAbility(FireStormAbility())
        registerAbility(OverloadAbility())
        registerAbility(WaterBurstAbility())
        registerAbility(BlackHoleAbility())
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

        if (ExtendedPlayer.from(player).gameState != ExtendedPlayer.GameState.KITS_IN_GAME) return

        val abilityId = item.persistentDataContainer.get(NamespacedKey(KitPvp.INSTANCE, "ability"), PersistentDataType.STRING)
        abilities[abilityId]?.handleAbility(player)
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        val player = event.player
        val item = event.itemDrop

        if (ExtendedPlayer.from(player).gameState != ExtendedPlayer.GameState.KITS_IN_GAME) return

        val abilityId = item.persistentDataContainer.get(NamespacedKey(KitPvp.INSTANCE, "ability"), PersistentDataType.STRING)
        abilities[abilityId]?.handleAbility(player)
    }

}
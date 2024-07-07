package io.github.chaosdave34.kitpvp.abilities

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.Utils
import io.github.chaosdave34.kitpvp.abilities.impl.*
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
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

    fun getItemAbilities(itemStack: ItemStack): List<Ability> {
        val container = itemStack.itemMeta.persistentDataContainer
        val key = NamespacedKey(KitPvp.INSTANCE, "abilities")

        if (container.has(key)) {
            val abilities = container.get(key, PersistentDataType.LIST.strings()) ?: return emptyList()

            val abilityList: MutableList<Ability> = ArrayList()

            for (id in abilities) {
                this.abilities[id]?.let { abilityList.add(it) }
            }
            return abilityList
        }
        return emptyList()
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return

        if (ExtendedPlayer.from(player).inSpawn()) return

        val container = item.itemMeta.persistentDataContainer
        val key = NamespacedKey(KitPvp.INSTANCE, "abilities")

        if (container.has(key)) {
            val abilities = getItemAbilities(item)
            val abilityTypes = abilities.stream().map(Ability::type).toList()

            for (ability in abilities) {
                when (ability.type) {
                    Ability.Type.RIGHT_CLICK -> {
                        if (event.action.isRightClick) {
                            if (abilityTypes.contains(Ability.Type.SNEAK_RIGHT_CLICK) && player.isSneaking) continue
                            ability.handleAbility(player)
                        }
                    }

                    Ability.Type.LEFT_CLICK -> {
                        if (event.action.isLeftClick) {
                            if (abilityTypes.contains(Ability.Type.SNEAK_LEFT_CLICK) && player.isSneaking) continue
                            ability.handleAbility(player)
                        }
                    }

                    Ability.Type.SNEAK_RIGHT_CLICK -> {
                        if (event.action.isRightClick && player.isSneaking) ability.handleAbility(player)
                    }

                    Ability.Type.SNEAK_LEFT_CLICK -> {
                        if (event.action.isLeftClick && player.isSneaking) ability.handleAbility(player)
                    }

                    else -> continue
                }
            }
        }
    }

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent) {
        val player = event.player
        if (!event.isSneaking) return
        if (ExtendedPlayer.from(player).inSpawn()) return

        for (armorContent in player.inventory.armorContents) {
            if (armorContent == null) continue

            val container = armorContent.itemMeta.persistentDataContainer
            val key = NamespacedKey(KitPvp.INSTANCE, "abilities")
            if (container.has(key)) {
                val abilities = container.get(key, PersistentDataType.LIST.strings()) ?: return
                for (id in abilities) {
                    if (this.abilities.containsKey(id)) {
                        val ability = this.abilities[id]

                        if (ability?.type == Ability.Type.SNEAK) ability.handleAbility(player)
                    }
                }
            }
        }
    }
}
package io.github.chaosdave34.kitpvp.abilities

import io.github.chaosdave34.ghutils.Utils
import io.github.chaosdave34.ghutils.persistentdatatypes.StringArrayPersistentDataType
import io.github.chaosdave34.kitpvp.ExtendedPlayer.Companion.from
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.impl.archer.LeapAbility
import io.github.chaosdave34.kitpvp.abilities.impl.artilleryman.EnhanceAbility
import io.github.chaosdave34.kitpvp.abilities.impl.assassin.HauntAbility
import io.github.chaosdave34.kitpvp.abilities.impl.creeper.ChargeAbility
import io.github.chaosdave34.kitpvp.abilities.impl.creeper.ExplodeAbility
import io.github.chaosdave34.kitpvp.abilities.impl.creeper.FireballAbility
import io.github.chaosdave34.kitpvp.abilities.impl.devil.FireStormAbility
import io.github.chaosdave34.kitpvp.abilities.impl.enderman.DragonFireballAbility
import io.github.chaosdave34.kitpvp.abilities.impl.enderman.EnderAttackAbility
import io.github.chaosdave34.kitpvp.abilities.impl.engineer.ModularShieldAbility
import io.github.chaosdave34.kitpvp.abilities.impl.engineer.OverloadAbility
import io.github.chaosdave34.kitpvp.abilities.impl.engineer.TurretAbility
import io.github.chaosdave34.kitpvp.abilities.impl.magician.LevitateAbility
import io.github.chaosdave34.kitpvp.abilities.impl.magician.ShuffleAbility
import io.github.chaosdave34.kitpvp.abilities.impl.poseidon.StormAbility
import io.github.chaosdave34.kitpvp.abilities.impl.poseidon.WaterBurstAbility
import io.github.chaosdave34.kitpvp.abilities.impl.provoker.NukeAbility
import io.github.chaosdave34.kitpvp.abilities.impl.provoker.SpookAbility
import io.github.chaosdave34.kitpvp.abilities.impl.tank.FortifyAbility
import io.github.chaosdave34.kitpvp.abilities.impl.tank.SeismicWaveAbility
import io.github.chaosdave34.kitpvp.abilities.impl.vampire.BatMorhpAbility
import io.github.chaosdave34.kitpvp.abilities.impl.zeus.LightningAbility
import io.github.chaosdave34.kitpvp.abilities.impl.zeus.RageAbility
import io.github.chaosdave34.kitpvp.abilities.impl.zeus.ThunderstormAbility
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack

class AbilityHandler : Listener {
    val abilities: MutableMap<String, Ability> = mutableMapOf()

    companion object {
        @JvmStatic
        lateinit var FIREBALL: Ability

        @JvmStatic
        lateinit var LIGHTNING: Ability

        @JvmStatic
        lateinit var THUNDERSTORM: Ability

        @JvmStatic
        lateinit var HAUNT: Ability

        @JvmStatic
        lateinit var LEVITATE: Ability

        @JvmStatic
        lateinit var SHUFFLE: Ability

        @JvmStatic
        lateinit var BAT_MORPH: Ability

        @JvmStatic
        lateinit var NUKE: Ability

        @JvmStatic
        lateinit var SPOOK: Ability

        @JvmStatic
        lateinit var EXPLODE: Ability

        @JvmStatic
        lateinit var ENDER_ATTACK: Ability

        @JvmStatic
        lateinit var STORM: Ability

        @JvmStatic
        lateinit var SEISMIC_WAVE: Ability

        @JvmStatic
        lateinit var FORTIFY: Ability

        @JvmStatic
        lateinit var ENHANCE: Ability

        @JvmStatic
        lateinit var TURRET: Ability

        @JvmStatic
        lateinit var LEAP: Ability

        @JvmStatic
        lateinit var DRAGON_FIREBALL: Ability

        @JvmStatic
        lateinit var FIRE_STORM: Ability

        @JvmStatic
        lateinit var RAGE: Ability

        @JvmStatic
        lateinit var MODULAR_SHIELD: Ability

        @JvmStatic
        lateinit var OVERLOAD: Ability

        @JvmStatic
        lateinit var CHARGE: Ability

        @JvmStatic
        lateinit var WATER_BURST: Ability
    }

    init {
        FIREBALL = registerAbility(FireballAbility())
        LIGHTNING = registerAbility(LightningAbility())
        THUNDERSTORM = registerAbility(ThunderstormAbility())
        HAUNT = registerAbility(HauntAbility())
        LEVITATE = registerAbility(LevitateAbility())
        SHUFFLE = registerAbility(ShuffleAbility())
        BAT_MORPH = registerAbility(BatMorhpAbility())
        NUKE = registerAbility(NukeAbility())
        SPOOK = registerAbility(SpookAbility())
        EXPLODE = registerAbility(ExplodeAbility())
        ENDER_ATTACK = registerAbility(EnderAttackAbility())
        STORM = registerAbility(StormAbility())
        SEISMIC_WAVE = registerAbility(SeismicWaveAbility())
        FORTIFY = registerAbility(FortifyAbility())
        ENHANCE = registerAbility(EnhanceAbility())
        TURRET = registerAbility(TurretAbility())
        LEAP = registerAbility(LeapAbility())
        DRAGON_FIREBALL = registerAbility(DragonFireballAbility())
        FIRE_STORM = registerAbility(FireStormAbility())
        RAGE = registerAbility(RageAbility())
        MODULAR_SHIELD = registerAbility(ModularShieldAbility())
        OVERLOAD = registerAbility(OverloadAbility())
        CHARGE = registerAbility(ChargeAbility())
        WATER_BURST = registerAbility(WaterBurstAbility())
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
            val abilities = container.get(key, StringArrayPersistentDataType()) ?: return emptyList()

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

        if (from(player).inSpawn()) return

        val container = item.itemMeta.persistentDataContainer
        val key = NamespacedKey(KitPvp.INSTANCE, "abilities")

        if (container.has(key)) {
            val abilities = getItemAbilities(event.item!!)
            val abilityTypes = abilities.stream().map(Ability::type).toList()

            for (ability in abilities) {
                when (ability.type) {
                    Ability.Type.RIGHT_CLICK -> {
                        if (event.action.isRightClick) {
                            if (abilityTypes.contains(Ability.Type.SNEAK_RIGHT_CLICK) && player.isSneaking) return
                            ability.handleAbility(player)
                        }
                    }
                    Ability.Type.LEFT_CLICK -> {
                        if (event.action.isLeftClick) {
                            if (abilityTypes.contains(Ability.Type.SNEAK_LEFT_CLICK) && player.isSneaking) return
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
        if (from(player).inSpawn()) return

        for (armorContent in player.inventory.armorContents) {
            if (armorContent == null) continue

            val container = armorContent.itemMeta.persistentDataContainer
            val key = NamespacedKey(KitPvp.INSTANCE, "abilities")
            if (container.has(key)) {
                val abilities = container.get(key, StringArrayPersistentDataType()) ?: return
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
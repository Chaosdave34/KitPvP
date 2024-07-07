package io.github.chaosdave34.kitpvp.damagetype

import io.github.chaosdave34.kitpvp.utils.ReflectionUtils
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.CraftRegistry
import org.bukkit.craftbukkit.damage.CraftDamageType
import org.bukkit.damage.DamageType
import net.minecraft.world.damagesource.DamageType as NmsDamageType

class DamageTypes {
    private var registry : Registry<NmsDamageType> = CraftRegistry.getMinecraftRegistry(Registries.DAMAGE_TYPE)

    companion object {
        lateinit var LAND: DamageType
        lateinit var ESCAPE: DamageType
        lateinit var BLACK_HOLE: DamageType
    }

    init {
        ReflectionUtils.unfreezeRegistry(registry)
        LAND = registerDamageType(NmsDamageType("land", DamageScaling.NEVER, 0f, DamageEffects.HURT))
        ESCAPE = registerDamageType(NmsDamageType("escape", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f, DamageEffects.HURT))
        BLACK_HOLE = registerDamageType(NmsDamageType("black_hole", DamageScaling.NEVER, 0f, DamageEffects.HURT))
    }

    private fun registerDamageType(damageType: NmsDamageType): DamageType {
        registry.createIntrusiveHolder(damageType)
        Registry.register(registry, NamespacedKey.minecraft(damageType.msgId).asString(), damageType)

        return CraftDamageType.minecraftToBukkit(damageType)
    }
}
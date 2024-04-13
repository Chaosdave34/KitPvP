package io.github.chaosdave34.kitpvp.damagetype

import io.github.chaosdave34.ghutils.utils.ReflectionUtils
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_20_R3.CraftRegistry
import org.bukkit.craftbukkit.v1_20_R3.damage.CraftDamageType
import org.bukkit.damage.DamageType
import net.minecraft.world.damagesource.DamageType as NmsDamageType

class DamageTypes {
    private var registry : Registry<NmsDamageType> = CraftRegistry.getMinecraftRegistry(Registries.DAMAGE_TYPE)

    companion object {
        @JvmStatic
        lateinit var LAND: DamageType

        @JvmStatic
        lateinit var ESCAPE: DamageType

        @JvmStatic
        lateinit var BLACK_HOLE: DamageType
    }

    init {
        ReflectionUtils.unfreezeRegistry(registry)

        LAND = registerDamageType(NmsDamageType("land", DamageScaling.NEVER, 0f, DamageEffects.HURT), "land")
        ESCAPE = registerDamageType(NmsDamageType("escape", DamageScaling.NEVER, 0f, DamageEffects.HURT), "escape")
        BLACK_HOLE = registerDamageType(NmsDamageType("black_hole", DamageScaling.NEVER, 0f, DamageEffects.HURT), "black_hole")
    }

    private fun registerDamageType(damageType: NmsDamageType, id: String): DamageType {
        registry.createIntrusiveHolder(damageType)
        Registry.register(registry, NamespacedKey.minecraft(id).asString(), damageType)

        return CraftDamageType.minecraftToBukkit(damageType)
    }
}
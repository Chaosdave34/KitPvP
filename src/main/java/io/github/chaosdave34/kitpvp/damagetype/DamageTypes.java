package io.github.chaosdave34.kitpvp.damagetype;

import io.github.chaosdave34.ghutils.utils.ReflectionUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R3.CraftRegistry;
import org.bukkit.craftbukkit.v1_20_R3.damage.CraftDamageType;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class DamageTypes {
    private final Registry<net.minecraft.world.damagesource.DamageType> registry;

    public static DamageType LAND;
    public static DamageType ESCAPE;

    public DamageTypes() {
        registry = CraftRegistry.getMinecraftRegistry(Registries.DAMAGE_TYPE);
        ReflectionUtils.unfreezeRegistry(registry);

        LAND = registerDamageType(new net.minecraft.world.damagesource.DamageType("land", DamageScaling.NEVER, 0, DamageEffects.HURT), "land");
        ESCAPE = registerDamageType(new net.minecraft.world.damagesource.DamageType("escape", DamageScaling.NEVER, 0, DamageEffects.HURT), "escape");
    }

    private DamageType registerDamageType(net.minecraft.world.damagesource.DamageType damageType, String id) {
        registry.createIntrusiveHolder(damageType);
        Registry.register(registry, NamespacedKey.minecraft(id).asString(), damageType);

        return CraftDamageType.minecraftToBukkit(damageType);
    }
}

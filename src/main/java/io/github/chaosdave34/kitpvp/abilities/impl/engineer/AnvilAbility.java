package io.github.chaosdave34.kitpvp.abilities.impl.engineer;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AnvilAbility extends Ability {
    public AnvilAbility() {
        super("anvil", "Anvil", Type.RIGHT_CLICK, 10);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Spawns an anvil above the",
                "player you are looking at",
                "in a 10 block radius."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        Entity target = p.getTargetEntity(10);
        if (target instanceof LivingEntity livingEntity) {

            if (target instanceof Player player) {
                if (ExtendedPlayer.from(player).inSpawn())
                    return false;
            }

            p.getWorld().spawnEntity(livingEntity.getLocation().add(0, 6, 0), EntityType.FALLING_BLOCK, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
                ((FallingBlock) entity).setBlockData(Material.ANVIL.createBlockData());
                entity.setMetadata("placed_by_player", new FixedMetadataValue(KitPvp.INSTANCE, true));
            });
            return true;
        }
        return false;
    }
}

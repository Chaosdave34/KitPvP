package net.gamershub.kitpvp.abilities.impl.engineer;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AnvilAbility extends Ability {
    public AnvilAbility() {
        super("anvil", "Anvil", AbilityType.RIGHT_CLICK, 10);
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
                if (KitPvpPlugin.INSTANCE.getExtendedPlayer(player).getGameState() == ExtendedPlayer.GameState.SPAWN)
                    return false;
            }

            p.getWorld().spawnEntity(livingEntity.getLocation().add(0, 6, 0), EntityType.FALLING_BLOCK, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
                ((FallingBlock) entity).setBlockData(Material.ANVIL.createBlockData());
                entity.setMetadata("placed_by_player", new FixedMetadataValue(KitPvpPlugin.INSTANCE, true));
            });
            return true;
        }
        return false;
    }
}

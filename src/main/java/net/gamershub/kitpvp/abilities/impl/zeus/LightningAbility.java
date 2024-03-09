package net.gamershub.kitpvp.abilities.impl.zeus;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightningAbility extends Ability {
    public LightningAbility() {
        super("lightning", "Lightning", AbilityType.RIGHT_CLICK, 3);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Strikes a lightning bold",
                "at the Enemy you are looking at",
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

            Location targetLocation = livingEntity.getLocation();
            targetLocation.getWorld().spawnEntity(targetLocation, EntityType.LIGHTNING, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
                ((LightningStrike) entity).setCausingPlayer(p);
                entity.setMetadata("ability", new FixedMetadataValue(KitPvpPlugin.INSTANCE, id));
            });
            return true;
        }
        return false;
    }

    @EventHandler
    public void onLightningImpact(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p && e.getDamager() instanceof LightningStrike lightningStrike) {
            if (lightningStrike.hasMetadata("ability")) {
                if (id.equals(lightningStrike.getMetadata("ability").get(0).value())) {
                    if (p.equals(lightningStrike.getCausingPlayer())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}

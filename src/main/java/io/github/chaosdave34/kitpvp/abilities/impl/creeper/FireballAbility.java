package io.github.chaosdave34.kitpvp.abilities.impl.creeper;

import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FireballAbility extends Ability {

    public FireballAbility() {
        super("fireball", "Fireball", Type.RIGHT_CLICK, 10);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Shoots a fireball.");
    }

    @Override
    public boolean onAbility(Player p) {
        Location loc = p.getEyeLocation();
        p.getWorld().spawnEntity(loc.add(loc.getDirection().normalize()), EntityType.FIREBALL, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
            Fireball fireball = (Fireball) entity;
            fireball.setDirection(p.getLocation().getDirection().multiply(1.5));
            fireball.setYield(2);
            fireball.setShooter(p);
            fireball.setMetadata("ability", new FixedMetadataValue(KitPvp.INSTANCE, getId()));
        });
        return true;
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Fireball fireball) {
            if (fireball.hasMetadata("ability")) {
                if (getId().equals(fireball.getMetadata("ability").get(0).value())) {
                    e.blockList().removeIf(block -> !block.hasMetadata("placed_by_player"));
                }
            }
        }
    }
}

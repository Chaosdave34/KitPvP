package io.github.chaosdave34.kitpvp.abilities.impl.provoker;

import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NukeAbility extends Ability {
    public NukeAbility() {
        super("nuke", "Nuke", AbilityType.RIGHT_CLICK, 120);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Send nukes!");
    }

    @Override
    public boolean onAbility(Player p) {
        Location location = p.getLocation();

        p.getWorld().spawnParticle(Particle.DRAGON_BREATH, location, 1500, 0, 70, 0, 0);

        location.setY(200);
        p.getWorld().spawnEntity(location, EntityType.FIREBALL, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity -> {
            Fireball fireball = (Fireball) entity;
            fireball.setMetadata("ability", new FixedMetadataValue(KitPvp.INSTANCE, id));
            fireball.setYield(10);
            fireball.setDirection(new Vector(0, -1, 0));
            fireball.setShooter(p);
        }));

        return true;
    }

    @EventHandler
    public void onImpact(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Fireball fireball) {
            if (fireball.hasMetadata("ability")) {
                if (id.equals(fireball.getMetadata("ability").get(0).value())) {
                    e.blockList().removeIf(block -> !block.hasMetadata("placed_by_player"));

                    World world = e.getEntity().getWorld();
                    Location location = e.getLocation();

                    world.spawnParticle(Particle.LAVA, location, 10000, 5, 5, 5, 1);
                    world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location, 5000, 5, 5, 5, 1);
                }
            }
        }
    }
}

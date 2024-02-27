package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NukeAbility extends Ability { // Todo: add particles on travel
    public NukeAbility() {
        super("nuke", "Nuke", AbilityType.LEFT_CLICK, 120);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return List.of(Component.text("Send nukes!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public boolean onAbility(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Location location = e.getPlayer().getLocation();

        p.getWorld().spawnParticle(Particle.DRAGON_BREATH, location, 1500, 0, 70, 0, 0);

        location.setY(200);
        p.getWorld().spawnEntity(location, EntityType.FIREBALL, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity -> {
            entity.setMetadata("ability", new FixedMetadataValue(KitPvpPlugin.INSTANCE, id));
            Fireball fireball = (Fireball) entity;
            fireball.setYield(10);
            fireball.setDirection(new Vector(0, -1, 0));
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

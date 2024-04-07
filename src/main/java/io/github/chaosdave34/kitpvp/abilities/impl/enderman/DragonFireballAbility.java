package io.github.chaosdave34.kitpvp.abilities.impl.enderman;

import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DragonFireballAbility extends Ability {

    public DragonFireballAbility() {
        super("dragon_fireball", "Dragon Fireball", Type.RIGHT_CLICK, 20);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Shoots a fireball.");
    }

    @Override
    public boolean onAbility(Player p) {
        Location loc = p.getEyeLocation();
        p.getWorld().spawnEntity(loc.add(loc.getDirection().normalize()), EntityType.DRAGON_FIREBALL, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
            DragonFireball fireball = (DragonFireball) entity;
            fireball.setDirection(p.getLocation().getDirection().multiply(1.5));
            fireball.setShooter(p);
            fireball.setMetadata("ability", new FixedMetadataValue(KitPvp.INSTANCE, getId()));
        });
        return true;
    }

    // Todo: fix this to just work for fireballs from this ability and not every area effect cloud
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof AreaEffectCloud areaEffectCloud) {
            if (areaEffectCloud.getSource() instanceof Player) {
                if (e.getEntity().getUniqueId().equals(areaEffectCloud.getOwnerUniqueId())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}

package net.gamershub.kitpvp.abilities.impl.creeper;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FireballAbility extends Ability {

    public FireballAbility() {
        super("fireball", "Fireball", AbilityType.RIGHT_CLICK, 10);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        List<Component> description = new ArrayList<>();
        description.add(Component.text("Shoots a fireball.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        return description;
    }

    @Override
    public boolean onAbility(Player p) {
        Location loc = p.getEyeLocation();
        p.getWorld().spawnEntity(loc.add(loc.getDirection().normalize()), EntityType.FIREBALL, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
            ((Fireball) entity).setDirection(p.getLocation().getDirection().multiply(1.5));
            ((Fireball) entity).setYield(2);
            entity.setMetadata("ability", new FixedMetadataValue(KitPvpPlugin.INSTANCE, id));
            entity.setMetadata("shot_by_player", new FixedMetadataValue(KitPvpPlugin.INSTANCE, p.getUniqueId()));
        });
        return true;
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Fireball fireball) {
            if (fireball.hasMetadata("ability")) {
                if (id.equals(fireball.getMetadata("ability").get(0).value())) {
                    e.blockList().removeIf(block -> !block.hasMetadata("placed_by_player"));
                }
            }
        }
    }
}
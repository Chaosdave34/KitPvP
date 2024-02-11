package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightningAbillity extends Ability {
    public LightningAbillity() {
        super(2, "Lightning", AbilityType.RIGHT_CLICK, 3);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return List.of(Component.text("Lets lighting strike on the block the block your looking at"));
    }

    @Override
    public void onAbility(PlayerInteractEvent e) {
        Location location = e.getPlayer().getLocation();
        location.getWorld().spawnEntity(location, EntityType.LIGHTNING);
    }
}

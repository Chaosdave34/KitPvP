package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LightningAbility extends Ability {
    public LightningAbility() {
        super("lightning","Lightning", AbilityType.RIGHT_CLICK, 3);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        List<Component> description = new ArrayList<>();
        description.add(Component.text("Strikes a lightning bold", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        description.add(Component.text("at the Enemy you are looking at", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        description.add(Component.text("in a 10 block radius.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        return description;
    }

    @Override
    public boolean onAbility(PlayerInteractEvent e) {
        Entity target = e.getPlayer().getTargetEntity(10);
        if (target != null) {
            Location targetLocation = target.getLocation();
            targetLocation.getWorld().spawnEntity(targetLocation, EntityType.LIGHTNING);
            return true;
        }
        return false;
    }
}

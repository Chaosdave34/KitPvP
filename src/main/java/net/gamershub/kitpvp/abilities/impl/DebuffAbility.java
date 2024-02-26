package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DebuffAbility extends Ability {
    public DebuffAbility() {
        super("debuff", "Debuff", AbilityType.LEFT_CLICK, 5);
    }


    @Override
    public @NotNull List<Component> getDescription() {
        List<Component> description = new ArrayList<>();
        description.add(Component.text("Applies slowness, nausea, blindness", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        description.add(Component.text("and weakness to all players in a", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        description.add(Component.text("10 block radius.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        return description;
    }

    @Override
    public boolean onAbility(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        for (Entity entity : p.getNearbyEntities(4, 4, 4)) {
            if (entity instanceof Player player) {
                player.addPotionEffect(PotionEffectType.SLOW.createEffect(100, 1));
                player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(100, 1));
                player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(100, 1));
                player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(100, 1));
            }
        }
        return true;
    }
}

package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LevitateAbility extends Ability {
    public LevitateAbility() {
        super("levitate", "Levitate", AbilityType.RIGHT_CLICK, 25);
    }


    @Override
    public @NotNull List<Component> getDescription() {
        return List.of(Component.text("Rise into the air!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public boolean onAbility(PlayerInteractEvent e) {
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 10));
        return true;
    }
}

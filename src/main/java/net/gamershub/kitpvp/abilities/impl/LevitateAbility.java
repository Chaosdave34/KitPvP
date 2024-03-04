package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
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
    public boolean onAbility(Player p) {
        p.getNearbyEntities(10, 10, 10).forEach(entity -> {
            if (entity instanceof Player target) {
                if (KitPvpPlugin.INSTANCE.getExtendedPlayer(target).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
                    target.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 10));
                }
            }
        });
        return true;
    }
}

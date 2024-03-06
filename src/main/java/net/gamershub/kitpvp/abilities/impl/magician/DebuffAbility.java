package net.gamershub.kitpvp.abilities.impl.magician;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DebuffAbility extends Ability {
    public DebuffAbility() {
        super("debuff", "Debuff", AbilityType.SNEAK_RIGHT_CLICK, 5);
    }


    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Applies slowness, nausea, blindness",
                "and weakness to all players in a",
                "4 block radius."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        for (Entity entity : p.getNearbyEntities(4, 4, 4)) {
            if (entity instanceof Player player) {
                player.addPotionEffect(PotionEffectType.SLOW.createEffect(5 * 20, 1));
                player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(5 * 20, 1));
                player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(5 * 20, 1));
                player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(5 * 20, 1));
            }
        }
        return true;
    }
}

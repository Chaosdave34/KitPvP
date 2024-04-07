package io.github.chaosdave34.kitpvp.abilities.impl.magician;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.abilities.Ability;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LevitateAbility extends Ability {
    public LevitateAbility() {
        super("levitate", "Levitate", Type.RIGHT_CLICK, 25);
    }


    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Let the players around you",
                "float into air."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        p.getNearbyEntities(10, 10, 10).forEach(entity -> {
            if (entity instanceof Player target) {
                if (ExtendedPlayer.from(target).inGame()) {
                    target.setVelocity(target.getVelocity().setY(5));
                }
            }
        });
        return true;
    }
}

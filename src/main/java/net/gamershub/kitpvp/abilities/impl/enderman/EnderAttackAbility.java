package net.gamershub.kitpvp.abilities.impl.enderman;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnderAttackAbility extends Ability {
    public EnderAttackAbility() {
        super("ender_attack", "Ender Attack", AbilityType.RIGHT_CLICK, 15);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Teleports you behind the player",
                "you are looking at in a 20",
                "block radius. Gain speed and ",
                "strength for 5 seconds."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        Player target = Utils.getTargetEntity(p, 20, Player.class, true);
        if (target != null) {

            if (KitPvpPlugin.INSTANCE.getExtendedPlayer(target).getGameState() == ExtendedPlayer.GameState.SPAWN)
                return false;

            p.teleport(target.getLocation().subtract(target.getLocation().getDirection()));

            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20, 1));

            return true;
        }
        return false;
    }
}

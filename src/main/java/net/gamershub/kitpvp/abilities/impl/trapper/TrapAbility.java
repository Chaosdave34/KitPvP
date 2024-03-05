package net.gamershub.kitpvp.abilities.impl.trapper;

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

public class TrapAbility extends Ability {
    public TrapAbility() {
        super("trap", "Trap", AbilityType.LEFT_CLICK, 10);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Traps the player you are",
                "looking at in a 10 block radius,"
        );
    }

    @Override
    public boolean onAbility(Player p) {
        Player target = Utils.getTargetEntity(p, 10, Player.class, true);
        if (target != null) {

            if (KitPvpPlugin.INSTANCE.getExtendedPlayer(target).getGameState() == ExtendedPlayer.GameState.SPAWN)
                return false;

            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 255));
            return true;
        }
        return false;
    }
}

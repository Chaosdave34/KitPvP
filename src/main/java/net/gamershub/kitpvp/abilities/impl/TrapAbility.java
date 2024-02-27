package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
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
        return List.of(
                Component.text("Traps the player you are", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("looking at in a 1o block radius,", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
        );
    }

    @Override
    public boolean onAbility(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Player target = Utils.getTargetEntity(p, 20, Player.class, true);
        if (target != null) {

            if (KitPvpPlugin.INSTANCE.getExtendedPlayer(target).getGameState() == ExtendedPlayer.GameState.SPAWN)
                return false;

            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 256));
            return true;
        }
        return false;
    }
}

package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AnvilAbility extends Ability {
    public AnvilAbility() {
        super("anvil", "Anvil", AbilityType.RIGHT_CLICK, 10);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return List.of(
                Component.text("Spawns an anvil above the", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("player you are looking at", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("in a 10 block radius.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
        );
    }

    @Override
    public boolean onAbility(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Player target = Utils.getTargetEntity(p, 20, Player.class, true);
        if (target != null) {

            if (KitPvpPlugin.INSTANCE.getExtendedPlayer(target).getGameState() == ExtendedPlayer.GameState.SPAWN)
                return false;

            target.getLocation().add(0, 6, 0).getBlock().setType(Material.ANVIL);
            return true;
        }
        return false;
    }
}

package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BatMorhpAbility extends Ability {
    public BatMorhpAbility() {
        super("bat_morph", "Bat Morph", AbilityType.RIGHT_CLICK, 30);
    }


    @Override
    public @NotNull List<Component> getDescription() {
        return List.of(Component.text("Morph into a bat.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public boolean onAbility(Player p) {
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        extendedPlayer.morph(EntityType.BAT);

        Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, extendedPlayer::unmorph, 200);

        return true;
    }
}

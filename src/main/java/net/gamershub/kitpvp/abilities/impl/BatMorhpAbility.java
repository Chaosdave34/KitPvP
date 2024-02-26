package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.player.PlayerInteractEvent;
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
    public boolean onAbility(PlayerInteractEvent e) {
        e.getPlayer().sendMessage(Component.text("This ability is not yet implemented!"));
        return false;
    }
}

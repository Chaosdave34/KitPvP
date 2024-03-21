package net.gamershub.kitpvp.textdisplays.impl;

import net.gamershub.kitpvp.textdisplays.TextDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AmusementParkTextDisplay extends TextDisplay {
    public AmusementParkTextDisplay() {
        super("world", new Location(null, -11.0, 120.5, 15.0), 1);
    }

    @Override
    public @NotNull List<Component> getLines(Player p) {
        return List.of(Component.literal("Amusement Park").withStyle(ChatFormatting.BOLD));
    }
}

package net.gamershub.kitpvp.textdisplay.impl;

import net.gamershub.kitpvp.textdisplay.TextDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HighestKillstreaksTextDisplay extends TextDisplay {
    public HighestKillstreaksTextDisplay() {
        super(new Location(Bukkit.getWorld("world"), 4.5, 101, -9.5), 6);
    }

    @Override
    public @NotNull List<Component> getLines(Player p) {
        return List.of(
                Component.literal("Highest Killstreaks").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD),
                Component.literal("1. ---"),
                Component.literal("2. ---"),
                Component.literal("3. ---"),
                Component.literal("4. ---"),
                Component.literal("5. ---")
        );
    }
}

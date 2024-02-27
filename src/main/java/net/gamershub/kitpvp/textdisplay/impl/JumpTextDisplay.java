package net.gamershub.kitpvp.textdisplay.impl;

import net.gamershub.kitpvp.textdisplay.TextDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JumpTextDisplay extends TextDisplay {
    public JumpTextDisplay() {
        super(new Location(Bukkit.getWorld("world"), 0.5, 100, 0.5), 1);
    }

    @Override
    public @NotNull List<net.minecraft.network.chat.Component> getLines(Player p) {
        return List.of(Component.literal("JUMP!").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
    }
}

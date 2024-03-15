package net.gamershub.kitpvp.textdisplays.impl;

import net.gamershub.kitpvp.textdisplays.TextDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfoTextDisplay extends TextDisplay {
    public InfoTextDisplay() {
        super(new Location(Bukkit.getWorld("world"), 4.5, 101.5, -9.5), 7);
    }

    @Override
    public @NotNull List<Component> getLines(Player p) {
        return List.of(
                Component.literal("Info:").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD),
                Component.literal("- Use /spawn to respawn"),
                Component.literal("- Placed blocks disappear"),
                Component.literal("after 5 seconds"),
                Component.literal("- Killing a player rewards you with"),
                Component.literal("consumable items (depending on your kit)"),
                Component.literal("- You gain XP for killing players")
        );
    }
}

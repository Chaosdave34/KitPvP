package io.github.chaosdave34.kitpvp.commands;

import io.github.chaosdave34.kitpvp.KitPvp;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(Component.text("Discord: " + KitPvp.INSTANCE.getConfig().get("discord")));
        return true;
    }
}

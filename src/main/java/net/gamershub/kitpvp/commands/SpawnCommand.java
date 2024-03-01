package net.gamershub.kitpvp.commands;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

            if (extendedPlayer.getCombatCooldown() == 0) {
                p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100.5, -8.5, 0, 0));
                KitPvpPlugin.INSTANCE.getExtendedPlayer(p).spawnPlayer();
            } else {
                p.sendMessage(Component.text("You are still in combat for " + extendedPlayer.getCombatCooldown() + "s!", NamedTextColor.RED));
            }
            return true;
        }
        return false;
    }
}

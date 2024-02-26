package net.gamershub.kitpvp.commands;

import net.gamershub.kitpvp.companions.Companion;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCompanionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            ServerLevel level = ((CraftWorld) p.getWorld()).getHandle();
            Companion companion = new Companion(p.getLocation(), p);
            level.addFreshEntity(companion);

            return true;
        }
        return false;
    }
}

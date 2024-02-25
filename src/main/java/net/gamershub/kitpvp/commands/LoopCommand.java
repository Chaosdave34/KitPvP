package net.gamershub.kitpvp.commands;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LoopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 3) return false;

        int count;
        int period;
        try {
            count = Integer.parseInt(args[0]);
            period = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (count < 1) return false;

        if (sender instanceof Player p) {
            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {
                    if (i < count)
                        p.performCommand(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
                    else {
                        p.sendMessage(Component.text("Done!"));
                        this.cancel();
                    }
                    i++;
                }
            }.runTaskTimer(KitPvpPlugin.INSTANCE, 0, period);
            return true;
        }

        return false;
    }
}

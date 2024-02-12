package net.gamershub.kitpvp.commands;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.items.CustomItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args.length > 2) return false;

            int amount = 1;
            if (args.length == 2) {
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            if (KitPvpPlugin.INSTANCE.getCustomItemHandler().ID_MAP.containsKey(args[0])) {
                CustomItem customItem = KitPvpPlugin.INSTANCE.getCustomItemHandler().ID_MAP.get(args[0]);
                p.getInventory().addItem(customItem.build(amount));
                return true;
            }
        }
        return false;
    }
}

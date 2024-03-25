package io.github.chaosdave34.kitpvp.commands;

import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args.length > 2 || args.length < 1) return false;

            int amount = 1;
            if (args.length == 2) {
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            if (KitPvp.INSTANCE.getCustomItemHandler().ID_MAP.containsKey(args[0])) {
                CustomItem customItem = KitPvp.INSTANCE.getCustomItemHandler().ID_MAP.get(args[0]);
                ItemStack item = customItem.build(amount);
                p.getInventory().addItem(item);

                p.sendMessage(Component.text("Gave you " + amount + " ")
                        .append(customItem.getName())
                        .append(Component.text(".")));
                return true;
            }
        }
        return false;
    }
}

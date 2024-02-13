package net.gamershub.kitpvp.commands;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class GenerateSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            StructureManager structureManager = Bukkit.getStructureManager();

            try {
                InputStream structureFile = KitPvpPlugin.INSTANCE.getResource("structures/spawn.nbt");

                if (structureFile != null) {
                    Structure structure = structureManager.loadStructure(structureFile);

                    structure.place(new Location(Bukkit.getWorld("world"), -12, 98, -12), false, StructureRotation.NONE, Mirror.NONE, 0, 1, new Random());

                    p.sendMessage(Component.text("Generated spawn centered at coordinates 0, 100, 0."));
                    return true;
                }
            } catch (IOException ignored) {
            }
        }

        return false;
    }
}

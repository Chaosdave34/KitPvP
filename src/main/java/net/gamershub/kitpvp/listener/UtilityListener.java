package net.gamershub.kitpvp.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.textdisplay.TextDisplayHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;

public class UtilityListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Component message = Component.text(p.getName() + " joined the party!", NamedTextColor.DARK_GRAY);
        e.joinMessage(message);

        //p.addResourcePack(UUID.randomUUID(), "https://vmd74965.contaboserver.net:8000/kitpvp.zip", HexFormat.of().parseHex("3cdfb428af81c083af7f23dfd387ee8b539a3c8a"), "FETT", true);

        KitPvpPlugin.INSTANCE.createExtendedPlayer(p);
        KitPvpPlugin.INSTANCE.getCustomItemHandler().updateCustomItems(p);

        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        // NPC
        KitPvpPlugin.INSTANCE.getFakePlayerHandler().spawnFakePlayers(p);

        // TextDisplay
        KitPvpPlugin.INSTANCE.getTextDisplayHandler().spawnTextDisplays(p);

        // Spawn
        extendedPlayer.spawnPlayer();
        extendedPlayer.updateDisplayName();

        // Highscores
        if (KitPvpPlugin.INSTANCE.getHighestKillstreaks().size() < 5) {
            KitPvpPlugin.INSTANCE.getHighestKillstreaks().put(p.getUniqueId(), extendedPlayer.getHighestKillStreak());
            KitPvpPlugin.INSTANCE.getTextDisplayHandler().updateTextDisplayForAll(TextDisplayHandler.HIGHEST_KILLSTREAKS);
        }
        if (KitPvpPlugin.INSTANCE.getHighestLevels().size() < 5) {
            KitPvpPlugin.INSTANCE.getHighestLevels().put(p.getUniqueId(), extendedPlayer.getLevel());
            KitPvpPlugin.INSTANCE.getTextDisplayHandler().updateTextDisplayForAll(TextDisplayHandler.HIGHEST_LEVELS);
        }


        p.sendPlayerListHeader(Component.text("KitPvP", NamedTextColor.YELLOW, TextDecoration.BOLD));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        KitPvpPlugin.INSTANCE.getExtendedPlayer(p).unmorph();
        KitPvpPlugin.INSTANCE.removeExtendedPlayer(p);

        Component message = Component.text(p.getName() + " left!", NamedTextColor.DARK_GRAY);
        e.quitMessage(message);
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();

        Component message = p.displayName()
                .append(Component.text(" >> "))
                .append(e.message());

        Bukkit.broadcast(message);
    }

    @EventHandler
    public void onGameSave(WorldSaveEvent e) {
        if (e.getWorld().getName().equals("world")) {
            KitPvpPlugin.INSTANCE.saveHighscores();

            for (Player p : Bukkit.getOnlinePlayers()) {
                ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
                Utils.writeObjectToFile(new File(KitPvpPlugin.INSTANCE.getDataFolder(), "player_data/" + p.getUniqueId() + ".json"), extendedPlayer);
            }
        }
    }
}

package net.gamershub.kitpvp.listener;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Component message = Component.text(p.getName() + " has joined the server!", NamedTextColor.GOLD);
        e.joinMessage(message);

        KitPvpPlugin.INSTANCE.createExtendedPlayer(p);
        KitPvpPlugin.INSTANCE.getPacketReader().inject(p);
        KitPvpPlugin.INSTANCE.getCustomItemHandler().updateCustomItems(p);

        // NPC
        KitPvpPlugin.INSTANCE.getFakePlayerHandler().spawnFakePlayers(p);

        // Spawn
        KitPvpPlugin.INSTANCE.getExtendedPlayer(p).spawnPlayer();

        p.sendPlayerListHeader(Component.text("KitPvP", NamedTextColor.YELLOW, TextDecoration.BOLD));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        KitPvpPlugin.INSTANCE.removeExtendedPlayer(p);

        Component message = Component.text(p.getName() + " has left the server!", NamedTextColor.GOLD);
        e.quitMessage(message);

        KitPvpPlugin.INSTANCE.getPacketReader().uninject(p);
    }
}

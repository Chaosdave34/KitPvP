package net.gamershub.kitpvp.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UtilityListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Component message = Component.text(p.getName() + " joined the party!", NamedTextColor.DARK_GRAY);
        e.joinMessage(message);

        KitPvpPlugin.INSTANCE.createExtendedPlayer(p);
        KitPvpPlugin.INSTANCE.getPacketReader().inject(p);
        KitPvpPlugin.INSTANCE.getCustomItemHandler().updateCustomItems(p);

        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        // NPC
        KitPvpPlugin.INSTANCE.getFakePlayerHandler().spawnFakePlayers(p);

        // Spawn
        extendedPlayer.spawnPlayer();
        extendedPlayer.updateDisplayName();

        p.sendPlayerListHeader(Component.text("KitPvP", NamedTextColor.YELLOW, TextDecoration.BOLD));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        KitPvpPlugin.INSTANCE.removeExtendedPlayer(p);

        Component message = Component.text(p.getName() + " left!", NamedTextColor.DARK_GRAY);
        e.quitMessage(message);

        KitPvpPlugin.INSTANCE.getPacketReader().uninject(p);
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
}

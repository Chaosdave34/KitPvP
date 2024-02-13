package net.gamershub.kitpvp.listener;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Component message = Component.text(p.getName() + " hat den Server betreten!", NamedTextColor.GOLD);
        e.joinMessage(message);

        p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100.5, -8.5, 0, 0));

        KitPvpPlugin.INSTANCE.createExtendedPlayer(p);
        KitPvpPlugin.INSTANCE.getPacketReader().inject(p);
        KitPvpPlugin.INSTANCE.getCustomItemHandler().updateCustomItems(p);

        // NPC
        KitPvpPlugin.INSTANCE.getFakePlayerHandler().spawnFakePlayers(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        KitPvpPlugin.INSTANCE.removeExtendedPlayer(p);

        Component message = Component.text(p.getName() + " hat den Server verlassen!", NamedTextColor.GOLD);
        e.quitMessage(message);

        KitPvpPlugin.INSTANCE.getPacketReader().uninject(p);
    }
}

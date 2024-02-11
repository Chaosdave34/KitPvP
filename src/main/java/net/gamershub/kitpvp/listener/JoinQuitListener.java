package net.gamershub.kitpvp.listener;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

        KitPvpPlugin.INSTANCE.getPacketReader().inject(p);

        // NPC
        KitPvpPlugin.INSTANCE.getFakePlayerHandler().spawnFakePlayers(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        Component message = Component.text(p.getName() + " hat den Server verlassen!", NamedTextColor.GOLD);
        e.quitMessage(message);

        KitPvpPlugin.INSTANCE.getPacketReader().uninject(p);
    }
}

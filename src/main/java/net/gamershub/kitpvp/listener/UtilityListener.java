package net.gamershub.kitpvp.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.textdisplays.TextDisplayHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

public class UtilityListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Component message = Component.text(p.getName() + " joined the party!", NamedTextColor.DARK_GRAY);
        e.joinMessage(message);

        KitPvpPlugin.INSTANCE.createExtendedPlayer(p);

        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);

        // resource pack
        String serverResourcePackUrl = KitPvpPlugin.INSTANCE.getConfig().getString("server_resource_pack_url");
        String serverResourcePackSha1sum = KitPvpPlugin.INSTANCE.getConfig().getString("server_resource_pack_sha1sum");
        if (serverResourcePackUrl != null && serverResourcePackSha1sum != null && serverResourcePackSha1sum.length() / 2 == 20)
            p.addResourcePack(UUID.fromString("9d309ee5-fcd8-4636-85cf-becfe3489018"), serverResourcePackUrl, HexFormat.of().parseHex(serverResourcePackSha1sum), "FETT", false);

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

        // player list header
        p.sendPlayerListHeader(Component.text("KitPvP", NamedTextColor.YELLOW, TextDecoration.BOLD));

        // daily challenges
        LocalDate lastLoginDate = Instant.ofEpochMilli(p.getLastLogin()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();

        if (!lastLoginDate.isEqual(today) || extendedPlayer.getDailyChallenges() == null || extendedPlayer.getDailyChallenges().isEmpty()) {
            extendedPlayer.updateDailyChallenges();
        }

        extendedPlayer.updatePlayerListFooter();

        List<Component> messages = List.of(
                Component.text("KitPvP is currently in closed beta!", NamedTextColor.AQUA),
                Component.text("- Feel free to report bugs and post feature requests in the #kitpvp channel on the discord server.", NamedTextColor.AQUA),
                Component.text("- ", NamedTextColor.AQUA)
                        .append(Component.text("Daily Challenges", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC))
                        .append(Component.text(" and ", NamedTextColor.AQUA))
                        .append(Component.text("Companions", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC))
                        .append(Component.text(" have been enabled. You may experience strange or unexpected behaviour.", NamedTextColor.AQUA))
        );

        Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, () -> messages.forEach(p::sendMessage), 10);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        extendedPlayer.unmorph();
        extendedPlayer.removeCompanion();
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
                ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
                Utils.writeObjectToFile(new File(KitPvpPlugin.INSTANCE.getDataFolder(), "player_data/" + p.getUniqueId() + ".json"), extendedPlayer);
            }
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        World world = e.getWorld();
        if (world.getName().equals("the_nether") || world.getName().equals("the_end")) Bukkit.unloadWorld(world, false);
    }
}

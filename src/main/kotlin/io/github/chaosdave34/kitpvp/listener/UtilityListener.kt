package io.github.chaosdave34.kitpvp.listener

import io.github.chaosdave34.ghutils.GHUtils
import io.github.chaosdave34.ghutils.utils.JsonUtils
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.textdisplays.TextDisplays
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.WorldCreator
import org.bukkit.block.data.type.DaylightDetector
import org.bukkit.block.data.type.DecoratedPot
import org.bukkit.block.data.type.Door
import org.bukkit.block.data.type.TrapDoor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldSaveEvent
import org.bukkit.generator.ChunkGenerator
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.function.Consumer


class UtilityListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        val message: Component = Component.text("${player.name} joined the party!", NamedTextColor.DARK_GRAY)
        event.joinMessage(message)

        val extendedPlayer = KitPvp.INSTANCE.createExtendedPlayer(player.uniqueId)

        // resource pack
        val serverResourcePackUrl = KitPvp.INSTANCE.config.getString("server_resource_pack_url")
        val serverResourcePackSha1sum = KitPvp.INSTANCE.config.getString("server_resource_pack_sha1sum")
        if (serverResourcePackUrl != null && serverResourcePackSha1sum != null && serverResourcePackSha1sum.length / 2 == 20) player.addResourcePack(
            UUID.fromString("9d309ee5-fcd8-4636-85cf-becfe3489018"),
            serverResourcePackUrl,
            HexFormat.of().parseHex(serverResourcePackSha1sum),
            "FETT",
            false
        )

        // NPC
        GHUtils.getFakePlayerHandler().spawnFakePlayers(player)

        // TextDisplay
        GHUtils.getTextDisplayHandler().spawnTextDisplays(player)

        // Spawn
        extendedPlayer.spawn()
        extendedPlayer.updateDisplayName()

        // Highscores
        if (KitPvp.INSTANCE.highestKillStreaksKits.size < 5) {
            KitPvp.INSTANCE.highestKillStreaksKits[player.uniqueId] = extendedPlayer.getHighestKillStreak(ExtendedPlayer.GameType.KITS)
            GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_KILL_STREAKS_KITS)
        }
        if (KitPvp.INSTANCE.highestKillStreaksElytra.size < 5) {
            KitPvp.INSTANCE.highestKillStreaksElytra[player.uniqueId] = extendedPlayer.getHighestKillStreak(ExtendedPlayer.GameType.ELYTRA)
            GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_KILL_STREAKS_ELYTRA)
        }

        if (KitPvp.INSTANCE.highestLevels.size < 5) {
            KitPvp.INSTANCE.highestLevels[player.uniqueId] = extendedPlayer.getLevel()
            GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_LEVELS_KITS)
        }

        // player list header
        player.sendPlayerListHeader(Component.text("KitPvP", NamedTextColor.YELLOW, TextDecoration.BOLD))

        // daily challenges
        val lastLoginDate = Instant.ofEpochMilli(player.lastLogin).atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()

        if (!lastLoginDate.isEqual(today) || extendedPlayer.dailyChallenges.isEmpty()) {
            extendedPlayer.updateDailyChallenges()
        }

        extendedPlayer.updatePlayerListFooter()

        val messages = listOf<Component>(
            Component.text("=================================================", NamedTextColor.GREEN),
            Component.text("KitPvP is currently in closed beta!", NamedTextColor.GREEN),
            Component.text("- Feel free to report bugs and post feature requests in the #kitpvp channel on the discord server.", NamedTextColor.GREEN),
            Component.text("- ", NamedTextColor.GREEN)
                .append(Component.text("Daily Challenges", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC))
                .append(Component.text(", ", NamedTextColor.GREEN))
                .append(Component.text("Companions", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC))
                .append(Component.text(" and ", NamedTextColor.GREEN))
                .append(Component.text("Bounties", NamedTextColor.GRAY).decorate(TextDecoration.ITALIC))
                .append(Component.text(" have been enabled. You may experience strange or unexpected behaviour.", NamedTextColor.GREEN)),
            Component.text("=================================================", NamedTextColor.GREEN)
        )

        Bukkit.getScheduler().runTaskLater(KitPvp.INSTANCE, Consumer { messages.forEach { message: Component -> player.sendMessage(message) } }, 10)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player

        val extendedPlayer = ExtendedPlayer.from(player)
        extendedPlayer.unMorph()
        extendedPlayer.removeCompanion()
        KitPvp.INSTANCE.removeExtendedPlayer(player.uniqueId)

        val message: Component = Component.text("${player.name} left!", NamedTextColor.DARK_GRAY)
        event.quitMessage(message)
    }

    @EventHandler
    fun onChatMessage(event: AsyncChatEvent) {
        event.isCancelled = true
        val p = event.player

        val message = p.displayName().append(Component.text(" >> ")).append(event.message())
        Bukkit.broadcast(message)
    }

    @EventHandler
    fun onGameSave(event: WorldSaveEvent) {
        if (event.world.name == "world") {
            KitPvp.INSTANCE.saveHighscores()

            for (player in Bukkit.getOnlinePlayers()) {
                val extendedPlayer = ExtendedPlayer.from(player)
                JsonUtils.writeObjectToFile(File(KitPvp.INSTANCE.dataFolder, "player_data/" + player.uniqueId + ".json"), extendedPlayer)
            }
        }
    }

    @EventHandler
    fun onWorldLoad(event: WorldLoadEvent) {
        val world = event.world
        if (world.name == "nether" || world.name == "the_end") Bukkit.unloadWorld(world, false)

        if (Bukkit.getWorld("world_elytra") == null) {
            val worldCreator = WorldCreator("world_elytra")
            worldCreator.generator(object : ChunkGenerator() {})
            val elytraPvp = Bukkit.getServer().createWorld(worldCreator)

            elytraPvp?.setGameRule(GameRule.DO_MOB_SPAWNING, false)
            elytraPvp?.setGameRule(GameRule.DO_FIRE_TICK, false)
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val extendedPlayer = ExtendedPlayer.from(player)
        if (extendedPlayer.gameState == ExtendedPlayer.GameState.DEBUG) return

        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val blockData = event.clickedBlock?.blockData
            if (blockData is Door || blockData is TrapDoor || blockData is DecoratedPot || blockData is DaylightDetector) {
                event.isCancelled = true
            }
        }
    }
}
package io.github.chaosdave34.kitpvp.listener

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.playerlist.PlayerListHandler
import io.github.chaosdave34.kitpvp.utils.JsonUtils
import io.papermc.paper.event.player.AsyncChatEvent
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.WorldCreator
import org.bukkit.block.data.type.Door
import org.bukkit.block.data.type.TrapDoor
import org.bukkit.entity.Painting
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldSaveEvent
import org.bukkit.generator.ChunkGenerator
import java.io.File
import java.util.*


class UtilityListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        val message: Component = Component.text("→ ${player.name}", NamedTextColor.DARK_GRAY)
        event.joinMessage(message)

        val extendedPlayer = KitPvp.INSTANCE.createExtendedPlayer(player.uniqueId)
        extendedPlayer.checkAndMigrateSaveData()

        // resource pack
        val serverResourcePackUrl = KitPvp.INSTANCE.config.getString("server_resource_pack_url")
        val serverResourcePackSha1sum = KitPvp.INSTANCE.config.getString("server_resource_pack_sha1sum")
        if (serverResourcePackUrl != null && serverResourcePackSha1sum != null && serverResourcePackSha1sum.length / 2 == 20) player.addResourcePack(
            UUID.fromString("9d309ee5-fcd8-4636-85cf-becfe3489018"),
            serverResourcePackUrl,
            HexFormat.of().parseHex(serverResourcePackSha1sum),
            null,
            false
        )

        // Spawn
        extendedPlayer.spawn()
        extendedPlayer.updateDisplayName()

        // Highscores
        if (KitPvp.INSTANCE.highestKillStreaksKits.size < 5 && extendedPlayer.getHighestKillStreak(ExtendedPlayer.GameType.KITS) > 0) {
            KitPvp.INSTANCE.highestKillStreaksKits[player.uniqueId] = extendedPlayer.getHighestKillStreak(ExtendedPlayer.GameType.KITS)
            //GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_KILL_STREAKS_KITS)
        }
        if (KitPvp.INSTANCE.highestKillStreaksElytra.size < 5 && extendedPlayer.getHighestKillStreak(ExtendedPlayer.GameType.ELYTRA) > 0) {
            KitPvp.INSTANCE.highestKillStreaksElytra[player.uniqueId] = extendedPlayer.getHighestKillStreak(ExtendedPlayer.GameType.ELYTRA)
            //GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_KILL_STREAKS_ELYTRA)
        }

        if (KitPvp.INSTANCE.highestLevels.size < 5 && extendedPlayer.getLevel() != 0) {
            KitPvp.INSTANCE.highestLevels[player.uniqueId] = extendedPlayer.getLevel()
            //GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_LEVELS_KITS)
        }

        // player list header
        player.sendPlayerListHeader(
            Component.text("==========[ KitPvP ]==========", NamedTextColor.YELLOW, TextDecoration.BOLD)
                .append(Component.newline())
        )

        PlayerListHandler().sendInitial(player)

        // daily challenges
//        val lastLoginDate = Instant.ofEpochMilli(player.lastLogin).atZone(ZoneId.systemDefault()).toLocalDate()
//        val today = LocalDate.now()
//
//        if (!lastLoginDate.isEqual(today) || extendedPlayer.dailyChallenges.isEmpty()) {
//            extendedPlayer.updateDailyChallenges()
//        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player

        val extendedPlayer = ExtendedPlayer.from(player)
        extendedPlayer.unMorph()
        extendedPlayer.removeCompanion()
        KitPvp.INSTANCE.removeExtendedPlayer(player.uniqueId)

        val message: Component = Component.text("← ${player.name}", NamedTextColor.DARK_GRAY)
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
        if (Bukkit.getWorld("world_elytra") == null) {
            val worldCreator = WorldCreator("world_elytra")
            worldCreator.generator(object : ChunkGenerator() {})
            Bukkit.getServer().createWorld(worldCreator)
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (ExtendedPlayer.from(player).gameState == ExtendedPlayer.GameState.DEBUG) return

        val blockData = event.clickedBlock?.blockData
        val material = event.clickedBlock?.type
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            if (blockData is Door || blockData is TrapDoor || material == Material.DAYLIGHT_DETECTOR) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onFlowerPotManipulate(event: PlayerFlowerPotManipulateEvent) {
        val player = event.player
        if (ExtendedPlayer.from(player).gameState == ExtendedPlayer.GameState.DEBUG) return

        event.isCancelled = true
    }

    @EventHandler
    fun onBreakPainting(event: HangingBreakEvent) {
        if (event.entity is Painting) event.isCancelled = true
    }
}
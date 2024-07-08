package io.github.chaosdave34.kitpvp.textdisplays

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.extensions.createTextDisplay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

object TextDisplays {
    lateinit var PERSONAL_STATISTICS_KITS: CustomTextDisplay
    lateinit var PERSONAL_STATISTICS_ELYTRA: CustomTextDisplay

    lateinit var HIGHEST_KILL_STREAKS_KITS: CustomTextDisplay
    lateinit var HIGHEST_KILL_STREAKS_ELYTRA: CustomTextDisplay

    lateinit var HIGHEST_LEVELS_KITS: CustomTextDisplay
    lateinit var HIGHEST_LEVELS_ELYTRA: CustomTextDisplay

    fun create() {
        val world = Bukkit.getWorld("world")
        if (world != null) {
            // Static
            // Jump
            world.createTextDisplay(Location(null, 2.0, 120.0, 2.0)) {
                it.defaultText = Component.text("JUMP", NamedTextColor.GREEN, TextDecoration.BOLD)
            }

            // Info
            world.createTextDisplay(Location(null, -1.5, 121.5, 11.5)) {
                val text = listOf(
                    Component.text("Info:", NamedTextColor.GOLD, TextDecoration.BOLD),
                    Component.text("- Use /spawn to respawn"),
                    Component.text("- Placed blocks disappear"),
                    Component.text("after 45 seconds"),
                    Component.text("- Killing a player rewards you with"),
                    Component.text("consumable items (depending on your kit)"),
                    Component.text("- You gain XP and coins for killing players")
                )

                it.defaultText = Component.join(JoinConfiguration.newlines(), text)
            }

            // Personal Statistics
            PERSONAL_STATISTICS_KITS = world.createTextDisplay(Location(null, 5.5, 121.5, 11.5)) {
                it.dynamicText = { player -> createPersonalStatisticsText(player, ExtendedPlayer.GameType.KITS) }
            }

            // Highest Kill Streaks
            HIGHEST_KILL_STREAKS_KITS = world.createTextDisplay(Location(null, 4.5, 121.5, -8.5)) {
                it.dynamicText = { _ -> createHighestKillStreaksText(ExtendedPlayer.GameType.KITS) }
            }

            // Highest Level
            HIGHEST_LEVELS_KITS = world.createTextDisplay(Location(null, 0.5, 121.5, -8.5)) {
                it.dynamicText = { _ -> createHighestLevelText() }
            }
        }

        val worldElytra = Bukkit.getWorld("world_elytra")
        if (worldElytra != null) {
            // Personal Statistics
            PERSONAL_STATISTICS_ELYTRA = worldElytra.createTextDisplay(Location(null, 10.5, 201.5, 4.5)) {
                it.dynamicText = { player -> createPersonalStatisticsText(player, ExtendedPlayer.GameType.ELYTRA) }
            }

            // Highest Kill Streaks
            HIGHEST_KILL_STREAKS_KITS = worldElytra.createTextDisplay(Location(null, 5.5, 201.5, 4.5)) {
                it.dynamicText = { _ -> createHighestKillStreaksText(ExtendedPlayer.GameType.ELYTRA) }
            }

            // Highest Level
            HIGHEST_LEVELS_ELYTRA = worldElytra.createTextDisplay(Location(null, 5.5, 201.5, -4.5)) {
                it.dynamicText = { _ -> createHighestLevelText() }
            }
        }
    }

    private fun createPersonalStatisticsText(player: Player, gameType: ExtendedPlayer.GameType): Component {
        val extendedPlayer = ExtendedPlayer.from(player)

        val highestKillStreak = extendedPlayer.getHighestKillStreak(gameType)
        val totalKills = extendedPlayer.getTotalKills(gameType)
        val totalDeaths = extendedPlayer.getTotalDeaths(gameType)
        val killDeathRatio = if (totalDeaths == 0) totalKills.toDouble() else totalKills.toDouble() / totalDeaths
        val roundedKillDeathRatio = (Math.round(killDeathRatio * 100).toDouble()) / 100

        val text = listOf(
            Component.text("Personal Statistics:", NamedTextColor.GOLD, TextDecoration.BOLD),
            Component.text("Highest Killstreak: $highestKillStreak"),
            Component.text("Kills: $totalKills"),
            Component.text("Deaths: $totalDeaths"),
            Component.text("K/D: $roundedKillDeathRatio")
        )

        return Component.join(JoinConfiguration.newlines(), text)
    }

    private fun createHighestKillStreaksText(gameType: ExtendedPlayer.GameType): Component {
        val lines: MutableList<Component> = mutableListOf()
        lines.add(Component.text("Highest Killstreaks:", NamedTextColor.GOLD, TextDecoration.BOLD))

        val entrySet = KitPvp.INSTANCE.getHighestKillStreaks(gameType).entries

        var i = 1
        for ((key, value) in entrySet.stream().sorted { v1: Map.Entry<UUID, Int>, v2: Map.Entry<UUID, Int> -> v2.value.compareTo(v1.value) }.toList()) {
            lines.add(Component.text("$i. ${Bukkit.getOfflinePlayer(key).name} - $value"))
            i++
        }

        for (j in i..5) lines.add(Component.text("$j. ---"))

        return Component.join(JoinConfiguration.newlines(), lines)
    }

    private fun createHighestLevelText(): Component {
        val lines: MutableList<Component> = mutableListOf()
        lines.add(Component.text("Highest Levels:", NamedTextColor.GOLD, TextDecoration.BOLD))

        val entrySet = KitPvp.INSTANCE.highestLevels.entries

        var i = 1
        for ((key, value) in entrySet.stream().sorted { v1: Map.Entry<UUID, Int>, v2: Map.Entry<UUID, Int> -> v2.value.compareTo(v1.value) }.toList()) {
            lines.add(Component.text("$i. ${Bukkit.getOfflinePlayer(key).name} - $value"))
            i++
        }

        for (j in i..5) lines.add(Component.text("$j. ---"))

        return Component.join(JoinConfiguration.newlines(), lines)
    }

    fun updateHighestLevels() {
        HIGHEST_LEVELS_KITS.updateTextForAll()
        HIGHEST_LEVELS_ELYTRA.updateTextForAll()
    }
}
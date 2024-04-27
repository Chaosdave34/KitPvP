package io.github.chaosdave34.kitpvp

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mojang.datafixers.util.Pair
import io.github.chaosdave34.ghutils.GHUtils
import io.github.chaosdave34.ghutils.utils.JsonUtils
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.challenges.ChallengesHandler
import io.github.chaosdave34.kitpvp.commands.*
import io.github.chaosdave34.kitpvp.companions.CompanionHandler
import io.github.chaosdave34.kitpvp.cosmetics.CosmeticHandler
import io.github.chaosdave34.kitpvp.customevents.CustomEventHandler
import io.github.chaosdave34.kitpvp.damagetype.DamageTypes
import io.github.chaosdave34.kitpvp.fakeplayer.FakePlayers
import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.ElytraKitHandler
import io.github.chaosdave34.kitpvp.kits.KitHandler
import io.github.chaosdave34.kitpvp.listener.*
import io.github.chaosdave34.kitpvp.textdisplays.TextDisplays
import io.github.chaosdave34.kitpvp.ultimates.UltimateHandler
import lombok.Getter
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.util.UUID

@Getter
class KitPvp : JavaPlugin() {
    private val extendedPlayers: MutableMap<UUID, ExtendedPlayer> = mutableMapOf()

    // Todo improve getters for high scores
    val highestKillStreaksKits: MutableMap<UUID, Int> = mutableMapOf()
    val highestKillStreaksElytra: MutableMap<UUID, Int> = mutableMapOf()

    val highestLevels: MutableMap<UUID, Int> = mutableMapOf()

    lateinit var abilityHandler: AbilityHandler
    lateinit var customItemHandler: CustomItemHandler
    lateinit var kitHandler: KitHandler
    lateinit var elytraKitHandler: ElytraKitHandler
    lateinit var companionHandler: CompanionHandler
    lateinit var cosmeticHandler: CosmeticHandler
    lateinit var customEventHandler: CustomEventHandler
    lateinit var challengesHandler: ChallengesHandler
    lateinit var ultimateHandler: UltimateHandler

    private lateinit var damageTypes: DamageTypes

    companion object {
        @JvmStatic
        lateinit var INSTANCE: KitPvp
    }

    override fun onEnable() {
        INSTANCE = this
        GHUtils.setPlugin(INSTANCE)

        abilityHandler = AbilityHandler()
        customItemHandler = CustomItemHandler()
        kitHandler = KitHandler()
        elytraKitHandler = ElytraKitHandler()
        companionHandler = CompanionHandler()
        cosmeticHandler = CosmeticHandler()
        customEventHandler = CustomEventHandler()
        challengesHandler = ChallengesHandler()
        ultimateHandler = UltimateHandler()

        damageTypes = DamageTypes()

        TextDisplays.create()
        FakePlayers.create()

        saveDefaultConfig()
        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")

        Bukkit.clearRecipes()

        // Setup worlds
        val overWorld = server.getWorld("world")
        overWorld?.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        overWorld?.setGameRule(GameRule.DO_FIRE_TICK, false)

        // Registering Listener
        val pluginManager = server.pluginManager

        pluginManager.registerEvents(UtilityListener(), this)
        pluginManager.registerEvents(SpawnListener(), this)
        pluginManager.registerEvents(GameListener(), this)
        pluginManager.registerEvents(GamePlayerDeathListener(), this)
        pluginManager.registerEvents(abilityHandler, this)
        pluginManager.registerEvents(companionHandler, this)
        pluginManager.registerEvents(cosmeticHandler, this)
        pluginManager.registerEvents(customEventHandler, this)
        pluginManager.registerEvents(EntityDamageListener(), this)


        // Registering Commands
        registerCommand("spawn", SpawnCommand())
        registerCommand("msg", MessageCommand(), PlayerTabCompleter())
        registerCommand("bounty", BountyCommand(), PlayerTabCompleter())
        registerCommand("discord", DiscordCommand())


        //Admin Commands
        registerCommand("loop", LoopCommand(), LoopTabCompleter())
        registerCommand("customitem", CustomItemCommand(), CustomItemTabCompleter())
        registerCommand("gommemode", GommeModeCommand())
        registerCommand("addexperience", AddExperienceCommand(), PlayerTabCompleter())
        registerCommand("addcoins", AddCoinsCommand(), PlayerTabCompleter())


        // Create data folder
        dataFolder.mkdir()
        File(dataFolder, "player_data").mkdir()


        // load high scores
        highestLevels.putAll(loadHighscore("highestLevels"))

        highestKillStreaksKits.putAll(loadHighscore("highestKillStreaksKits"))
        highestKillStreaksElytra.putAll(loadHighscore("highestKillStreaksElytra"))
    }

    private fun registerCommand(name: String, executor: CommandExecutor) {
        registerCommand(name, executor, EmptyTabCompleter())
    }

    private fun registerCommand(name: String, executor: CommandExecutor, tabCompleter: TabCompleter) {
        val command = getCommand(name)
        if (command == null) {
            logger.warning("Failed to register command $name!")
            return
        }

        command.setExecutor(executor)
        command.tabCompleter = tabCompleter
    }

    // Extended players
    fun getExtendedPlayer(uuid: UUID): ExtendedPlayer {
        if (!extendedPlayers.contains(uuid)) {
            val extendedPlayer = createExtendedPlayer(uuid)
            return extendedPlayer
        }

        return extendedPlayers[uuid] ?: createExtendedPlayer(uuid)
    }

    fun createExtendedPlayer(uuid: UUID): ExtendedPlayer {
        val playerData = File(dataFolder, "player_data/$uuid.json")

        var extendedPlayer: ExtendedPlayer? = null
        if (playerData.exists()) extendedPlayer = JsonUtils.readObjectFromFile(playerData, ExtendedPlayer::class.java)
        if (extendedPlayer == null) extendedPlayer = ExtendedPlayer(uuid)

        extendedPlayers[uuid] = extendedPlayer

        return extendedPlayer
    }

    fun removeExtendedPlayer(uuid: UUID) {
        val extendedPlayer = getExtendedPlayer(uuid)

        JsonUtils.writeObjectToFile(File(dataFolder, "player_data/$uuid.json"), extendedPlayer)

        extendedPlayers.remove(uuid)
    }

    // Highscores
    fun saveHighscores() {
        JsonUtils.writeObjectToFile(File(dataFolder, "highestLevels"), highestLevels)

        JsonUtils.writeObjectToFile(File(dataFolder, "highestKillStreaksKits"), highestKillStreaksKits)
        JsonUtils.writeObjectToFile(File(dataFolder, "highestKillStreaksElytra"), highestKillStreaksElytra)
    }

    private fun loadHighscore(name: String): MutableMap<UUID, Int> {
        try {
            val bufferedReader = File(dataFolder, name).bufferedReader()
            val jsonString = bufferedReader.use { it.readText() }
            bufferedReader.close()

            val highscore: MutableMap<UUID, Int>? = Gson().fromJson(jsonString, object : TypeToken<MutableMap<UUID, Int>?>() {})

            return highscore ?: mutableMapOf()
        } catch (e: IOException) {
            logger.warning("Error while reading high scores from file! {${e.message}}")
        }
        return mutableMapOf()
    }

    fun getHighestKillStreaks(gameType: ExtendedPlayer.GameType?): MutableMap<UUID, Int> {
        return when (gameType) {
            ExtendedPlayer.GameType.KITS -> highestKillStreaksKits
            ExtendedPlayer.GameType.ELYTRA -> highestKillStreaksElytra
            else -> mutableMapOf()
        }
    }

    override fun onDisable() {
        saveHighscores()

        for (extendedPlayer in extendedPlayers.values) {
            val uuid = extendedPlayer.getPlayer()?.uniqueId ?: continue
            extendedPlayer.unMorph()
            extendedPlayer.removeCompanion()
            JsonUtils.writeObjectToFile(File(dataFolder, "player_data/$uuid.json"), extendedPlayer)
        }

        val iterator: MutableIterator<Map.Entry<Location, Pair<Long, BlockData>>> = GameListener.blocksToRemove.entries.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()
            val block = entry.key.block
            block.blockData = entry.value.second
            iterator.remove()
        }

        customEventHandler.activeEvent?.stop()
    }

}
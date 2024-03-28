package io.github.chaosdave34.kitpvp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.chaosdave34.ghutils.GHUtils;
import io.github.chaosdave34.ghutils.utils.JsonUtils;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.challenges.ChallengesHandler;
import io.github.chaosdave34.kitpvp.commands.*;
import io.github.chaosdave34.kitpvp.companions.CompanionHandler;
import io.github.chaosdave34.kitpvp.cosmetics.CosmeticHandler;
import io.github.chaosdave34.kitpvp.customevents.CustomEventHandler;
import io.github.chaosdave34.kitpvp.damagetype.DamageTypes;
import io.github.chaosdave34.kitpvp.fakeplayer.FakePlayers;
import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.ElytraKitHandler;
import io.github.chaosdave34.kitpvp.kits.KitHandler;
import io.github.chaosdave34.kitpvp.listener.GameListener;
import io.github.chaosdave34.kitpvp.listener.GamePlayerDeathListener;
import io.github.chaosdave34.kitpvp.listener.SpawnListener;
import io.github.chaosdave34.kitpvp.listener.UtilityListener;
import io.github.chaosdave34.kitpvp.textdisplays.TextDisplays;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public final class KitPvp extends JavaPlugin {
    public static KitPvp INSTANCE;

    private final Map<UUID, ExtendedPlayer> extendedPlayers = new HashMap<>();

    private final Map<UUID, Integer> highestKillstreaks = new HashMap<>();
    private final Map<UUID, Integer> highestLevels = new HashMap<>();

    private AbilityHandler abilityHandler;
    private CustomItemHandler customItemHandler;
    private KitHandler kitHandler;
    private ElytraKitHandler elytraKitHandler;
    private CompanionHandler companionHandler;
    private CosmeticHandler cosmeticHandler;
    private CustomEventHandler customEventHandler;
    private ChallengesHandler challengesHandler;

    private DamageTypes damageTypes;

    private GameListener gameListener;

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    @Override
    public void onEnable() {
        INSTANCE = this;
        GHUtils.setPlugin(INSTANCE);

        abilityHandler = new AbilityHandler();
        customItemHandler = new CustomItemHandler();
        kitHandler = new KitHandler();
        elytraKitHandler = new ElytraKitHandler();
        companionHandler = new CompanionHandler();
        cosmeticHandler = new CosmeticHandler();
        customEventHandler = new CustomEventHandler();
        challengesHandler = new ChallengesHandler();

        damageTypes = new DamageTypes();

        gameListener = new GameListener();

        TextDisplays.create();
        FakePlayers.create();

        saveDefaultConfig();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Bukkit.clearRecipes();

        // Setup world
        World overworld = getServer().getWorld("world");
        if (overworld != null) {
            overworld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        }

        // Registering Listener
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new UtilityListener(), this);

        pluginManager.registerEvents(new SpawnListener(), this);

        pluginManager.registerEvents(gameListener, this);
        pluginManager.registerEvents(new GamePlayerDeathListener(), this);

        pluginManager.registerEvents(abilityHandler, this);
        pluginManager.registerEvents(cosmeticHandler, this);
        pluginManager.registerEvents(customEventHandler, this);

        // Registering Commands
        registerCommand("spawn", new SpawnCommand());
        registerCommand("msg", new MessageCommand(), new PlayerTabCompleter());
        registerCommand("bounty", new BountyCommand(), new PlayerTabCompleter());
        registerCommand("discord", new DiscordCommand());

        //Admin Commands
        registerCommand("loop", new LoopCommand(), new LoopTabCompleter());
        registerCommand("customitem", new CustomItemCommand(), new CustomItemTabCompleter());
        registerCommand("gommemode", new GommeModeCommand());
        registerCommand("addexperience", new AddExperienceCommand(), new PlayerTabCompleter());
        registerCommand("addcoins", new AddCoinsCommand(), new PlayerTabCompleter());

        // Create data folder
        getDataFolder().mkdir();
        new File(getDataFolder(), "player_data").mkdir();

        // load high scores
        highestLevels.putAll(loadHighescore("highestLevels"));
        highestKillstreaks.putAll(loadHighescore("highestKillstreaks"));
    }

    private void registerCommand(String name, CommandExecutor executor) {
        registerCommand(name, executor, new EmptyTabCompleter());
    }

    private void registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            getLogger().warning("Failed to register command " + name + "!");
            return;
        }

        command.setExecutor(executor);
        command.setTabCompleter(tabCompleter);
    }

    public ExtendedPlayer getExtendedPlayer(Player p) {
        return extendedPlayers.get(p.getUniqueId());
    }

    public void createExtendedPlayer(Player p) {
        File playerData = new File(getDataFolder(), "player_data/" + p.getUniqueId() + ".json");

        ExtendedPlayer extendedPlayer = null;
        if (playerData.exists())
            extendedPlayer = JsonUtils.readObjectFromFile(playerData, ExtendedPlayer.class);
        if (extendedPlayer == null)
            extendedPlayer = new ExtendedPlayer(p);

        extendedPlayers.put(p.getUniqueId(), extendedPlayer);
    }

    public void removeExtendedPlayer(Player p) {
        ExtendedPlayer extendedPlayer = getExtendedPlayer(p);

        JsonUtils.writeObjectToFile(new File(getDataFolder(), "player_data/" + p.getUniqueId() + ".json"), extendedPlayer);

        extendedPlayers.remove(p.getUniqueId());
    }

    @Override
    public void onDisable() {
        saveHighscores();

        for (ExtendedPlayer extendedPlayer : extendedPlayers.values()) {
            extendedPlayer.unmorph();
            extendedPlayer.removeCompanion();
            JsonUtils.writeObjectToFile(new File(getDataFolder(), "player_data/" + extendedPlayer.getPlayer().getUniqueId() + ".json"), extendedPlayer);
        }

        for (Location location : gameListener.getBlocksToRemove().keySet()) {
            Block block = location.getBlock();
            if (block.getBlockData() instanceof Waterlogged waterlogged) {
                waterlogged.setWaterlogged(false);
                block.setBlockData(waterlogged);
            } else
                block.setType(Material.AIR);
        }

        if (customEventHandler.getActiveEvent() != null)
            customEventHandler.getActiveEvent().stop();
    }

    public void saveHighscores() {
        JsonUtils.writeObjectToFile(new File(getDataFolder(), "highestLevels"), highestLevels);
        JsonUtils.writeObjectToFile(new File(getDataFolder(), "highestKillstreaks"), highestKillstreaks);
    }

    private Map<UUID, Integer> loadHighescore(String name) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(GHUtils.PLUGIN.getDataFolder(), name));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            @SuppressWarnings("Convert2Diamond")
            Map<UUID, Integer> highscore = new Gson().fromJson(stringBuilder.toString(), new TypeToken<Map<UUID, Integer>>() {
            });

            return highscore == null ? Collections.emptyMap() : highscore;


        } catch (IOException e) {
            GHUtils.PLUGIN.getLogger().warning("Error while reading high scores from file! " + e.getMessage());
        }
        return Collections.emptyMap();
    }

}

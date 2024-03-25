package io.github.chaosdave34.kitpvp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import io.github.chaosdave34.ghlib.GHLib;
import io.github.chaosdave34.ghlib.Utils;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.challenges.ChallengesHandler;
import io.github.chaosdave34.kitpvp.commands.*;
import io.github.chaosdave34.kitpvp.companions.CompanionHandler;
import io.github.chaosdave34.kitpvp.cosmetics.CosmeticHandler;
import io.github.chaosdave34.kitpvp.customevents.CustomEventHandler;
import io.github.chaosdave34.kitpvp.fakeplayer.FakePlayers;
import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.KitHandler;
import io.github.chaosdave34.kitpvp.listener.GameListener;
import io.github.chaosdave34.kitpvp.listener.GamePlayerDeathListener;
import io.github.chaosdave34.kitpvp.listener.SpawnListener;
import io.github.chaosdave34.kitpvp.listener.UtilityListener;
import io.github.chaosdave34.kitpvp.textdisplays.TextDisplays;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
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
    private CompanionHandler companionHandler;
    private CosmeticHandler cosmeticHandler;
    private CustomEventHandler customEventHandler;
    private ChallengesHandler challengesHandler;

    private GameListener gameListener;

    @SuppressWarnings({"ResultOfMethodCallIgnored", "DataFlowIssue"})
    @Override
    public void onEnable() {
        INSTANCE = this;
        GHLib.setPlugin(INSTANCE);

        abilityHandler = new AbilityHandler();
        customItemHandler = new CustomItemHandler();
        kitHandler = new KitHandler();
        companionHandler = new CompanionHandler();
        cosmeticHandler = new CosmeticHandler();
        customEventHandler = new CustomEventHandler();
        challengesHandler = new ChallengesHandler();

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
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("spawn").setTabCompleter(new EmptyTabCompleter());

        getCommand("msg").setExecutor(new MessageCommand());
        getCommand("msg").setTabCompleter(new PlayerTabCompleter());

        getCommand("loop").setExecutor(new LoopCommand());
        getCommand("loop").setTabCompleter(new LoopTabCompleter());

        getCommand("custom_item").setExecutor(new CustomItemCommand());
        getCommand("custom_item").setTabCompleter(new CustomItemTabCompleter());

        getCommand("set_game_state").setExecutor(new SetGameStateCommand());
        getCommand("set_game_state").setTabCompleter(new SetGameStateTabCompleter());

        getCommand("add_experience").setExecutor(new AddExperienceCommand());
        getCommand("add_experience").setTabCompleter(new PlayerTabCompleter());

        // Create data folder
        getDataFolder().mkdir();
        new File(getDataFolder(), "player_data").mkdir();

        // load high scores
        highestLevels.putAll(loadHighescore("highestLevels"));
        highestKillstreaks.putAll(loadHighescore("highestKillstreaks"));
    }

    public ExtendedPlayer getExtendedPlayer(Player p) {
        return extendedPlayers.get(p.getUniqueId());
    }

    public void createExtendedPlayer(Player p) {
        ExtendedPlayer extendedPlayer = Utils.readObjectFromFile(new File(getDataFolder(), "player_data/" + p.getUniqueId() + ".json"), ExtendedPlayer.class);
        if (extendedPlayer == null)
            extendedPlayer = new ExtendedPlayer(p);

        extendedPlayers.put(p.getUniqueId(), extendedPlayer);
    }

    public void removeExtendedPlayer(Player p) {
        ExtendedPlayer extendedPlayer = getExtendedPlayer(p);

        Utils.writeObjectToFile(new File(getDataFolder(), "player_data/" + p.getUniqueId() + ".json"), extendedPlayer);

        extendedPlayers.remove(p.getUniqueId());
    }

    @Override
    public void onDisable() {
        saveHighscores();

        for (ExtendedPlayer extendedPlayer : extendedPlayers.values()) {
            extendedPlayer.unmorph();
            extendedPlayer.removeCompanion();
            Utils.writeObjectToFile(new File(getDataFolder(), "player_data/" + extendedPlayer.getPlayer().getUniqueId() + ".json"), extendedPlayer);
        }

        for (Location location : gameListener.getBlocksToRemove().keySet()) {
            Block block = location.getBlock();
            if (block.getBlockData() instanceof Waterlogged waterlogged) {
                waterlogged.setWaterlogged(false);
                block.setBlockData(waterlogged);
            } else
                block.setType(Material.AIR);
        }
    }

    public void saveHighscores() {
        Utils.writeObjectToFile(new File(getDataFolder(), "highestLevels"), highestLevels);
        Utils.writeObjectToFile(new File(getDataFolder(), "highestKillstreaks"), highestKillstreaks);
    }

    private Map<UUID, Integer> loadHighescore(String name) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(GHLib.PLUGIN.getDataFolder(), name));
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
            GHLib.PLUGIN.getLogger().warning("Error while reading high scores from file! " + e.getMessage());
        }
        return Collections.emptyMap();
    }

}

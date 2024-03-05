package net.gamershub.kitpvp;

import lombok.Getter;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.commands.*;
import net.gamershub.kitpvp.enchantments.CustomEnchantmentHandler;
import net.gamershub.kitpvp.fakeplayer.FakePlayerHandler;
import net.gamershub.kitpvp.gui.GuiHandler;
import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.KitHandler;
import net.gamershub.kitpvp.listener.GameListener;
import net.gamershub.kitpvp.listener.GamePlayerDeathListener;
import net.gamershub.kitpvp.listener.SpawnListener;
import net.gamershub.kitpvp.listener.UtilityListener;
import net.gamershub.kitpvp.textdisplay.TextDisplayHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Getter
public final class KitPvpPlugin extends JavaPlugin {
    public static KitPvpPlugin INSTANCE;

    private final Map<UUID, ExtendedPlayer> extendedPlayers = new HashMap<>();

    private final Map<UUID, Integer> highestKillstreaks = new HashMap<>();
    private final Map<UUID, Integer> highestLevels = new HashMap<>();

    private GuiHandler guiHandler;
    private CustomEnchantmentHandler customEnchantmentHandler;
    private AbilityHandler abilityHandler;
    private CustomItemHandler customItemHandler;
    private KitHandler kitHandler;
    private FakePlayerHandler fakePlayerHandler;
    private TextDisplayHandler textDisplayHandler;

    @SuppressWarnings({"DataFlowIssue", "ResultOfMethodCallIgnored"})
    @Override
    public void onEnable() {
        INSTANCE = this;

        guiHandler = new GuiHandler();
        customEnchantmentHandler = new CustomEnchantmentHandler();
        abilityHandler = new AbilityHandler();
        customItemHandler = new CustomItemHandler();
        kitHandler = new KitHandler();
        fakePlayerHandler = new FakePlayerHandler();
        textDisplayHandler = new TextDisplayHandler();

        // Setup world
        World overworld = getServer().getWorld("world");
        if (overworld != null) {
            overworld.setGameRule(GameRule.KEEP_INVENTORY, true);
            overworld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        }

        Bukkit.clearRecipes();

        // Registering Listener
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new UtilityListener(), this);
        pluginManager.registerEvents(new SpawnListener(), this);
        pluginManager.registerEvents(new GameListener(), this);
        pluginManager.registerEvents(new GamePlayerDeathListener(), this);
        pluginManager.registerEvents(guiHandler, this);
        pluginManager.registerEvents(customEnchantmentHandler, this);
        pluginManager.registerEvents(abilityHandler, this);
        pluginManager.registerEvents(fakePlayerHandler, this);

        // Registering Commands
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("spawn").setTabCompleter(new EmptyTabCompleter());

        getCommand("msg").setExecutor(new MessageCommand());
        getCommand("msg").setTabCompleter(new PlayerTabCompleter());

        getCommand("loop").setExecutor(new LoopCommand());
        getCommand("loop").setTabCompleter(new LoopTabCompleter());

        getCommand("custom_item").setExecutor(new CustomItemCommand());
        getCommand("custom_item").setTabCompleter(new CustomItemTabCompleter());

        getCommand("generate_spawn").setExecutor(new GenerateSpawnCommand());
        getCommand("generate_spawn").setTabCompleter(new EmptyTabCompleter());

        getCommand("set_game_state").setExecutor(new SetGameStateCommand());
        getCommand("set_game_state").setTabCompleter(new SetGameStateTabCompleter());

        getCommand("add_experience").setExecutor(new AddExperienceCommand());
        getCommand("add_experience").setTabCompleter(new PlayerTabCompleter());

        getCommand("test_companion").setExecutor(new TestCompanionCommand());
        getCommand("test_companion").setTabCompleter(new EmptyTabCompleter());

        // Create data folder
        getDataFolder().mkdir();
        new File(getDataFolder(), "player_data").mkdir();

        // load high scores
        highestLevels.putAll(Utils.loadHighescore("highestLevels"));
        highestKillstreaks.putAll(Utils.loadHighescore("highestKillstreaks"));
    }

    public ExtendedPlayer getExtendedPlayer(Player p) {
        return extendedPlayers.get(p.getUniqueId());
    }

    public void createExtendedPlayer(Player p) {
        ExtendedPlayer extendedPlayer = Utils.readObjectFromFile(new File(getDataFolder(), "player_data/" + p.getUniqueId() + ".json"), ExtendedPlayer.class);
        if (extendedPlayer == null) {
            extendedPlayer = new ExtendedPlayer(p);
        }

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
    }


    public void saveHighscores () {
        Utils.writeObjectToFile(new File(getDataFolder(), "highestLevels"), highestLevels);
        Utils.writeObjectToFile(new File(getDataFolder(), "highestKillstreaks"), highestKillstreaks);
    }

}

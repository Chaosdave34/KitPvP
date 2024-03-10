package net.gamershub.kitpvp;

import lombok.Getter;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.challenges.ChallengesHandler;
import net.gamershub.kitpvp.commands.*;
import net.gamershub.kitpvp.companions.CompanionHandler;
import net.gamershub.kitpvp.cosmetics.CosmeticHandler;
import net.gamershub.kitpvp.customevents.CustomEventHandler;
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

    private CustomEnchantmentHandler customEnchantmentHandler;
    private AbilityHandler abilityHandler;
    private CustomItemHandler customItemHandler;
    private KitHandler kitHandler;
    private CompanionHandler companionHandler;
    private FakePlayerHandler fakePlayerHandler;
    private TextDisplayHandler textDisplayHandler;
    private CosmeticHandler cosmeticHandler;
    private GuiHandler guiHandler;
    private CustomEventHandler customEventHandler;
    private ChallengesHandler challengesHandler;

    @SuppressWarnings({"DataFlowIssue", "ResultOfMethodCallIgnored"})
    @Override
    public void onEnable() {
        INSTANCE = this;

        customEnchantmentHandler = new CustomEnchantmentHandler();
        abilityHandler = new AbilityHandler();
        customItemHandler = new CustomItemHandler();
        kitHandler = new KitHandler();
        companionHandler = new CompanionHandler();
        fakePlayerHandler = new FakePlayerHandler();
        textDisplayHandler = new TextDisplayHandler();
        cosmeticHandler = new CosmeticHandler();
        guiHandler = new GuiHandler();
        customEventHandler = new CustomEventHandler();
        challengesHandler = new ChallengesHandler();

        // Setup world
        World overworld = getServer().getWorld("world");
        if (overworld != null) {
            overworld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        }

        Bukkit.clearRecipes();

        // Registering Listener
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new UtilityListener(), this);
        pluginManager.registerEvents(new SpawnListener(), this);
        pluginManager.registerEvents(new GameListener(), this);
        pluginManager.registerEvents(new GamePlayerDeathListener(), this);
        pluginManager.registerEvents(customEnchantmentHandler, this);
        pluginManager.registerEvents(abilityHandler, this);
        pluginManager.registerEvents(fakePlayerHandler, this);
        pluginManager.registerEvents(cosmeticHandler, this);
        pluginManager.registerEvents(guiHandler, this);

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
            Utils.writeObjectToFile(new File(getDataFolder(), "player_data/" +  extendedPlayer.getPlayer().getUniqueId() + ".json"), extendedPlayer);
        }
    }

    public void saveHighscores() {
        Utils.writeObjectToFile(new File(getDataFolder(), "highestLevels"), highestLevels);
        Utils.writeObjectToFile(new File(getDataFolder(), "highestKillstreaks"), highestKillstreaks);
    }

}

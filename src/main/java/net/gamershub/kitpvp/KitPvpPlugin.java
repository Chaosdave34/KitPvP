package net.gamershub.kitpvp;

import lombok.Getter;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.commands.*;
import net.gamershub.kitpvp.enchantments.EnchantmentHandler;
import net.gamershub.kitpvp.fakeplayer.FakePlayerHandler;
import net.gamershub.kitpvp.gui.GuiHandler;
import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.KitHandler;
import net.gamershub.kitpvp.listener.JoinQuitListener;
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

    private PacketReader packetReader;
    private FakePlayerHandler fakePlayerHandler;
    private GuiHandler guiHandler;
    private EnchantmentHandler enchantmentHandler;
    private AbilityHandler abilityHandler;
    private CustomItemHandler customItemHandler;
    private KitHandler kitHandler;

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onEnable() {
        INSTANCE = this;

        packetReader = new PacketReader();
        fakePlayerHandler = new FakePlayerHandler();
        guiHandler = new GuiHandler();
        enchantmentHandler = new EnchantmentHandler();
        abilityHandler = new AbilityHandler();
        customItemHandler = new CustomItemHandler();
        kitHandler = new KitHandler();

        // Setup world
        World overworld = getServer().getWorld("world");
        if (overworld != null) {
            overworld.setGameRule(GameRule.KEEP_INVENTORY, true);
            overworld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        }

        // Registering Listener
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new JoinQuitListener(), this);
        pluginManager.registerEvents(guiHandler, this);
        pluginManager.registerEvents(enchantmentHandler, this);
        pluginManager.registerEvents(abilityHandler, this);

        // Registering Commands
        getCommand("loop").setExecutor(new LoopCommand());
        getCommand("loop").setTabCompleter(new LoopTabCompleter());

        getCommand("custom_item").setExecutor(new CustomItemCommand());
        getCommand("custom_item").setTabCompleter(new CustomItemTabCompleter());

        getCommand("test_kit").setExecutor(new TestKitCommand());
        getCommand("test_kit").setTabCompleter(new EmptyTabCompleter());

        KitPvpPlugin.INSTANCE.getLogger().info("Added " + enchantmentHandler.getBukkitEnchantments().size() + " custom Enchantments!");
        KitPvpPlugin.INSTANCE.getLogger().info("Added " + KitPvpPlugin.INSTANCE.getCustomItemHandler().ID_MAP.size() + " custom Items!");

        // Create data folder
        getDataFolder().mkdir();
        new File(getDataFolder(), "player_data").mkdir();
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

    @SuppressWarnings("RedundantMethodOverride")
    @Override
    public void onDisable() {
    }

}

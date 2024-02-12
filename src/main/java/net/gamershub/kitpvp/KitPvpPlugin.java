package net.gamershub.kitpvp;

import lombok.Getter;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.commands.*;
import net.gamershub.kitpvp.enchantments.EnchantmentHandler;
import net.gamershub.kitpvp.fakeplayer.FakePlayerHandler;
import net.gamershub.kitpvp.gui.GuiHandler;
import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.listener.JoinQuitListener;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


@Getter
public final class KitPvpPlugin extends JavaPlugin {
    public static KitPvpPlugin INSTANCE;
    private PacketReader packetReader;
    private FakePlayerHandler fakePlayerHandler;
    private GuiHandler guiHandler;
    private EnchantmentHandler enchantmentHandler;
    private AbilityHandler abilityHandler;
    private CustomItemHandler customItemHandler;

    @Override
    public void onEnable() {
        INSTANCE = this;

        packetReader = new PacketReader();
        fakePlayerHandler = new FakePlayerHandler();
        guiHandler = new GuiHandler();
        enchantmentHandler = new EnchantmentHandler();
        abilityHandler = new AbilityHandler();
        customItemHandler = new CustomItemHandler();

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
        getCommand("testnpc").setExecutor(new TestNPCCommand());
        getCommand("testinv").setExecutor(new TestInventoryCommand());

        getCommand("loop").setExecutor(new LoopCommand());
        getCommand("loop").setTabCompleter(new LoopTabCompleter());

        getCommand("custom_item").setExecutor(new CustomItemCommand());
        getCommand("custom_item").setTabCompleter(new CustomItemTabCompleter());

        KitPvpPlugin.INSTANCE.getLogger().info("Added " + enchantmentHandler.getBukkitEnchantments().size() + " custom Enchantments!");
        KitPvpPlugin.INSTANCE.getLogger().info("Added " + KitPvpPlugin.INSTANCE.getCustomItemHandler().ID_MAP.size() + " custom Items!");

    }

    @SuppressWarnings("RedundantMethodOverride")
    @Override
    public void onDisable() {
    }

}

package net.gamershub.kitpvp.items;

import net.gamershub.kitpvp.PDCUtils;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.items.impl.artilleryman.Jetpack;
import net.gamershub.kitpvp.items.impl.artilleryman.RocketLauncher;
import net.gamershub.kitpvp.items.impl.assassin.AssassinSword;
import net.gamershub.kitpvp.items.impl.creeper.CreeperLeggings;
import net.gamershub.kitpvp.items.impl.creeper.FireballSword;
import net.gamershub.kitpvp.items.impl.devil.DevilsSword;
import net.gamershub.kitpvp.items.impl.enderman.EnderSword;
import net.gamershub.kitpvp.items.impl.engineer.TrapWand;
import net.gamershub.kitpvp.items.impl.engineer.TurretItem;
import net.gamershub.kitpvp.items.impl.magician.MagicWand;
import net.gamershub.kitpvp.items.impl.poseidon.PoseidonsTrident;
import net.gamershub.kitpvp.items.impl.provoker.NukeItem;
import net.gamershub.kitpvp.items.impl.provoker.SpookSword;
import net.gamershub.kitpvp.items.impl.tank.TankAxe;
import net.gamershub.kitpvp.items.impl.tank.TankBoots;
import net.gamershub.kitpvp.items.impl.vampire.VampireSword;
import net.gamershub.kitpvp.items.impl.zeus.LightningWand;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomItemHandler {
    public Map<String, CustomItem> ID_MAP = new HashMap<>();

    public static CustomItem FIREBALL_SWORD;
    public static CustomItem LIGHTNING_WAND;
    public static CustomItem MAGIC_WAND;
    public static CustomItem VAMPIRE_SWORD;
    public static CustomItem NUKE;
    public static CustomItem SPOOK_SWORD;
    public static CustomItem TRAP_WAND;
    public static CustomItem CREEPER_LEGGINGS;
    public static CustomItem ENDER_SWORD;
    public static CustomItem POSEIDONS_TRIDENT;
    public static CustomItem DEVILS_SWORD;
    public static CustomItem TANK_BOOTS;
    public static CustomItem TANK_AXE;
    public static CustomItem JETPACK;
    public static CustomItem ASSASSIN_SWORD;
    public static CustomItem ROCKET_LAUNCHER;
    public static CustomItem TURRET;

    public CustomItemHandler() {
        FIREBALL_SWORD = registerItem(new FireballSword());
        LIGHTNING_WAND = registerItem(new LightningWand());
        MAGIC_WAND = registerItem(new MagicWand());
        VAMPIRE_SWORD = registerItem(new VampireSword());
        NUKE = registerItem(new NukeItem());
        SPOOK_SWORD = registerItem(new SpookSword());
        TRAP_WAND = registerItem(new TrapWand());
        CREEPER_LEGGINGS = registerItem(new CreeperLeggings());
        ENDER_SWORD = registerItem(new EnderSword());
        POSEIDONS_TRIDENT = registerItem(new PoseidonsTrident());
        DEVILS_SWORD = registerItem(new DevilsSword());
        TANK_BOOTS = registerItem(new TankBoots());
        TANK_AXE = registerItem(new TankAxe());
        JETPACK = registerItem(new Jetpack());
        ASSASSIN_SWORD = registerItem(new AssassinSword());
        ROCKET_LAUNCHER = registerItem(new RocketLauncher());
        TURRET = registerItem(new TurretItem());
    }

    private CustomItem registerItem(CustomItem item) {
        Utils.registerEvents(item);
        ID_MAP.put(item.id, item);
        return item;
    }

    @Nullable
    public static String getCustomItemId(@NotNull ItemStack itemStack) {
        return PDCUtils.getId(itemStack.getItemMeta());
    }
}

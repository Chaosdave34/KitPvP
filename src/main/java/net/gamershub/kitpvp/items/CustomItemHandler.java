package net.gamershub.kitpvp.items;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.items.impl.assassin.AssassinSword;
import net.gamershub.kitpvp.items.impl.assassin.HauntWand;
import net.gamershub.kitpvp.items.impl.creeper.CreeperLeggings;
import net.gamershub.kitpvp.items.impl.creeper.FireballWand;
import net.gamershub.kitpvp.items.impl.crossbow.Jetpack;
import net.gamershub.kitpvp.items.impl.devil.DevilsSword;
import net.gamershub.kitpvp.items.impl.enderman.EnderSword;
import net.gamershub.kitpvp.items.impl.magician.MagicWand;
import net.gamershub.kitpvp.items.impl.poseidon.PoseidonsTrident;
import net.gamershub.kitpvp.items.impl.provoker.NukeItem;
import net.gamershub.kitpvp.items.impl.provoker.SpookSword;
import net.gamershub.kitpvp.items.impl.tank.TankAxe;
import net.gamershub.kitpvp.items.impl.tank.TankBoots;
import net.gamershub.kitpvp.items.impl.trapper.TrapWand;
import net.gamershub.kitpvp.items.impl.vampire.VampireSword;
import net.gamershub.kitpvp.items.impl.zeus.LightningWand;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class CustomItemHandler {
    public Map<String, CustomItem> ID_MAP = new HashMap<>();

    public static CustomItem FIREBALL;
    public static CustomItem LIGHTNING_WAND;
    public static CustomItem HAUNT_WAND;
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

    public CustomItemHandler() {
        FIREBALL = createItem(new FireballWand());
        LIGHTNING_WAND = createItem(new LightningWand());
        HAUNT_WAND = createItem(new HauntWand());
        MAGIC_WAND = createItem(new MagicWand());
        VAMPIRE_SWORD = createItem(new VampireSword());
        NUKE = createItem(new NukeItem());
        SPOOK_SWORD = createItem(new SpookSword());
        TRAP_WAND = createItem(new TrapWand());
        CREEPER_LEGGINGS = createItem(new CreeperLeggings());
        ENDER_SWORD = createItem(new EnderSword());
        POSEIDONS_TRIDENT = createItem(new PoseidonsTrident());
        DEVILS_SWORD = createItem(new DevilsSword());
        TANK_BOOTS = createItem(new TankBoots());
        TANK_AXE = createItem(new TankAxe());
        JETPACK = createItem(new Jetpack());
        ASSASSIN_SWORD = createItem(new AssassinSword());
    }

    private CustomItem createItem(CustomItem item) {
        Utils.registerEvents(item);
        ID_MAP.put(item.id, item);
        return item;
    }

    public static String getCustomItemId(ItemStack itemStack) {
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(KitPvpPlugin.INSTANCE, "id");
        if (container.has(key)) {
            return container.get(key, PersistentDataType.STRING);
        }
        return null;
    }
}

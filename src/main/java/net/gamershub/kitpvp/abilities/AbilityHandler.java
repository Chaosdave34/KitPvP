package net.gamershub.kitpvp.abilities;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.impl.creeper.ExplodeAbility;
import net.gamershub.kitpvp.abilities.impl.creeper.FireballAbility;
import net.gamershub.kitpvp.abilities.impl.enderman.EnderAttackAbility;
import net.gamershub.kitpvp.abilities.impl.magician.DebuffAbility;
import net.gamershub.kitpvp.abilities.impl.magician.LevitateAbility;
import net.gamershub.kitpvp.abilities.impl.magician.MagicAttackAbility;
import net.gamershub.kitpvp.abilities.impl.poseidon.StormAbility;
import net.gamershub.kitpvp.abilities.impl.provoker.NukeAbility;
import net.gamershub.kitpvp.abilities.impl.provoker.SpookAbility;
import net.gamershub.kitpvp.abilities.impl.runner.HauntAbility;
import net.gamershub.kitpvp.abilities.impl.tank.FortifyAbility;
import net.gamershub.kitpvp.abilities.impl.tank.SeismicWaveAbility;
import net.gamershub.kitpvp.abilities.impl.trapper.AnvilAbility;
import net.gamershub.kitpvp.abilities.impl.trapper.TrapAbility;
import net.gamershub.kitpvp.abilities.impl.vampire.BatMorhpAbility;
import net.gamershub.kitpvp.abilities.impl.zeus.LightningAbility;
import net.gamershub.kitpvp.abilities.impl.zeus.ThunderstormAbility;
import net.gamershub.kitpvp.persistentdatatype.StringArrayPersistentDataType;
import net.gamershub.kitpvp.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AbilityHandler implements Listener {
    public HashMap<String, Ability> abilities = new HashMap<>();

    public static Ability FIREBALL;
    public static Ability LIGHTNING;
    public static Ability THUNDERSTORM;
    public static Ability HAUNT;
    public static Ability LEVITATE;
    public static Ability DEBUFF;
    public static Ability BAT_MORPH;
    public static Ability NUKE;
    public static Ability SPOOK;
    public static Ability ANVIL;
    public static Ability TRAP;
    public static Ability EXPLODE;
    public static Ability ENDER_ATTACK;
    public static Ability STORM;
    public static Ability SEISMIC_WAVE;
    public static Ability FORTIFY;
    public static Ability MAGIC_ATTACK;

    public AbilityHandler() {
        FIREBALL = registerAbility(new FireballAbility());
        LIGHTNING = registerAbility(new LightningAbility());
        THUNDERSTORM = registerAbility(new ThunderstormAbility());
        HAUNT = registerAbility(new HauntAbility());
        LEVITATE = registerAbility(new LevitateAbility());
        DEBUFF = registerAbility(new DebuffAbility());
        BAT_MORPH = registerAbility(new BatMorhpAbility());
        NUKE = registerAbility(new NukeAbility());
        SPOOK = registerAbility(new SpookAbility());
        ANVIL = registerAbility(new AnvilAbility());
        TRAP = registerAbility(new TrapAbility());
        EXPLODE = registerAbility(new ExplodeAbility());
        ENDER_ATTACK = registerAbility(new EnderAttackAbility());
        STORM = registerAbility(new StormAbility());
        SEISMIC_WAVE = registerAbility(new SeismicWaveAbility());
        FORTIFY = registerAbility(new FortifyAbility());
        MAGIC_ATTACK = registerAbility(new MagicAttackAbility());
    }

    public Ability registerAbility(Ability ability) {
        Utils.registerEvents(ability);

        if (!abilities.containsKey(ability.getId())) {
            abilities.put(ability.getId(), ability);
        } else {
            throw new IllegalArgumentException("Ability ID already in use");
        }

        return ability;
    }

    public List<Ability> getItemAbilities(ItemStack itemStack) {
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(KitPvpPlugin.INSTANCE, "abilities");

        if (container.has(key)) {
            String[] abilities = container.get(key, new StringArrayPersistentDataType());
            if (abilities == null) return Collections.emptyList();

            List<Ability> abilityList = new ArrayList<>();

            for (String id : abilities) {
                abilityList.add(this.abilities.get(id));
            }

            return abilityList;

        }

        return Collections.emptyList();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() == null) return;

        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN)
            return;

        PersistentDataContainer container = e.getItem().getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(KitPvpPlugin.INSTANCE, "abilities");

        if (container.has(key)) {
            String[] abilities = container.get(key, new StringArrayPersistentDataType());
            if (abilities == null) return;
            for (String id : abilities) {
                if (this.abilities.containsKey(id)) {
                    Ability ability = this.abilities.get(id);

                    switch (ability.getType()) {
                        case RIGHT_CLICK -> {
                            if (e.getAction().isRightClick()) ability.handleAbility(p);
                        }
                        case LEFT_CLICK -> {
                            if (e.getAction().isLeftClick()) ability.handleAbility(p);
                        }
                        case SNEAK_RIGHT_CLICK -> {
                            if (e.getAction().isRightClick() && p.isSneaking()) ability.handleAbility(p);
                        }
                        case SNEAK_LEFT_CLICK -> {
                            if (e.getAction().isLeftClick() && p.isSneaking()) ability.handleAbility(p);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();

        if (!e.isSneaking()) return;

        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.SPAWN) return;

        for (ItemStack armorContent : p.getInventory().getArmorContents()) {
            if (armorContent == null) continue;

            PersistentDataContainer container = armorContent.getItemMeta().getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(KitPvpPlugin.INSTANCE, "abilities");
            if (container.has(key)) {
                String[] abilities = container.get(key, new StringArrayPersistentDataType());
                if (abilities == null) return;
                for (String id : abilities) {
                    if (this.abilities.containsKey(id)) {
                        Ability ability = this.abilities.get(id);

                        if (ability.getType() == AbilityType.SNEAK) ability.handleAbility(p);

                    }
                }
            }
        }
    }
}

package io.github.chaosdave34.kitpvp.abilities;

import io.github.chaosdave34.ghutils.Utils;
import io.github.chaosdave34.ghutils.persistentdatatypes.StringArrayPersistentDataType;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.impl.archer.LeapAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.artilleryman.EnhanceAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.assassin.HauntAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.creeper.ExplodeAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.creeper.FireballAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.enderman.DragonFireballAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.enderman.EnderAttackAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.engineer.AnvilAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.engineer.TrapAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.engineer.TurretAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.magician.ShuffleAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.magician.LevitateAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.poseidon.StormAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.provoker.NukeAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.provoker.SpookAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.tank.FortifyAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.tank.SeismicWaveAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.vampire.BatMorhpAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.zeus.LightningAbility;
import io.github.chaosdave34.kitpvp.abilities.impl.zeus.ThunderstormAbility;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

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
    public static Ability SHUFFLE;
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
    public static Ability ENHANCE;
    public static Ability TURRET;
    public static Ability LEAP;
    public static Ability DRAGON_FIREBALL;

    public AbilityHandler() {
        FIREBALL = registerAbility(new FireballAbility());
        LIGHTNING = registerAbility(new LightningAbility());
        THUNDERSTORM = registerAbility(new ThunderstormAbility());
        HAUNT = registerAbility(new HauntAbility());
        LEVITATE = registerAbility(new LevitateAbility());
        SHUFFLE = registerAbility(new ShuffleAbility());
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
        ENHANCE = registerAbility(new EnhanceAbility());
        TURRET = registerAbility(new TurretAbility());
        LEAP = registerAbility(new LeapAbility());
        DRAGON_FIREBALL = registerAbility(new DragonFireballAbility());
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

    public @NotNull List<Ability> getItemAbilities(ItemStack itemStack) {
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(KitPvp.INSTANCE, "abilities");

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

        if (ExtendedPlayer.from(p).inSpawn())
            return;

        PersistentDataContainer container = e.getItem().getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(KitPvp.INSTANCE, "abilities");

        if (container.has(key)) {
            List<Ability> abilities = getItemAbilities(e.getItem());
            List<AbilityType> abilityTypes = abilities.stream().map(Ability::getType).toList();

            for (Ability ability : abilities) {
                switch (ability.getType()) {
                    case RIGHT_CLICK -> {
                        if (e.getAction().isRightClick()) {
                            if (abilityTypes.contains(AbilityType.SNEAK_RIGHT_CLICK) && p.isSneaking()) return;
                            ability.handleAbility(p);
                        }
                    }
                    case LEFT_CLICK -> {
                        if (e.getAction().isLeftClick()) {
                            if (abilityTypes.contains(AbilityType.SNEAK_LEFT_CLICK) && p.isSneaking()) return;
                            ability.handleAbility(p);
                        }
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

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();

        if (!e.isSneaking()) return;

        if (ExtendedPlayer.from(p).inSpawn()) return;

        for (ItemStack armorContent : p.getInventory().getArmorContents()) {
            if (armorContent == null) continue;

            PersistentDataContainer container = armorContent.getItemMeta().getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(KitPvp.INSTANCE, "abilities");
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

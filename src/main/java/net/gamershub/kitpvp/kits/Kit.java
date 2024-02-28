package net.gamershub.kitpvp.kits;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

@Getter
public class Kit implements Listener {
    private final String id;
    private final String name;

    public Kit(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public double getMaxHealth() {
        return 20;
    }

    public double getMovementSpeed() {
        return 0.1;
    }

    public ItemStack getHeadContent() {
        return null;
    }

    public ItemStack getChestContent() {
        return null;
    }

    public ItemStack getLegsContent() {
        return null;
    }

    public ItemStack getFeetContent() {
        return null;
    }

    public ItemStack getOffhandContent() {
        return null;
    }

    public ItemStack[] getInventoryContent() {
        return new ItemStack[]{};
    }


    public ItemStack[] getKillRewards() {
        return new ItemStack[]{};
    }

    public List<Pair<PotionEffectType, Integer>> getPotionEffects() {
        return Collections.emptyList();
    }

    public void apply(Player p) {
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        extendedPlayer.setSelectedKitId(id);

        AttributeInstance maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null) maxHealth.setBaseValue(getMaxHealth());
        p.setHealth(getMaxHealth());

        AttributeInstance movementSpeed = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (movementSpeed != null) movementSpeed.setBaseValue(getMovementSpeed());

        PlayerInventory inv = p.getInventory();

        inv.setContents(getInventoryContent());
        inv.setHelmet(getHeadContent());
        inv.setChestplate(getChestContent());
        inv.setLeggings(getLegsContent());
        inv.setBoots(getFeetContent());
        inv.setItemInOffHand(getOffhandContent());
        inv.addItem(getKillRewards());

        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }

        for (Pair<PotionEffectType, Integer> potionEffect : getPotionEffects()) {
            p.addPotionEffect(new PotionEffect(potionEffect.getFirst(), -1, potionEffect.getSecond(), false, false, false));
        }
    }

    protected void setLeatherArmorColor(ItemStack leatherArmor, Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) leatherArmor.getItemMeta();
        leatherArmorMeta.setColor(color);
        leatherArmor.setItemMeta(leatherArmorMeta);
    }
}

package io.github.chaosdave34.kitpvp.kits;

import com.mojang.datafixers.util.Pair;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
public abstract class ElytraKit extends Kit {
    protected Material icon;

     public ElytraKit(String id, String name, Material icon) {
         super(id, name);
         this.icon = icon;
     }

    @Override
    public ItemStack getChestContent() {
        return new ItemStack(Material.ELYTRA);
    }

    @Override
    public ItemStack getOffhandContent() {
         ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET, 3);
         firework.editMeta(FireworkMeta.class, fireworkMeta -> fireworkMeta.setPower(2));

        return firework;
    }

    @Override
    public void apply(Player p) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        extendedPlayer.setSelectedElytraKitId(id);

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

        inv.setItem(17, new ItemStack(Material.ARROW));

        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }

        for (Pair<PotionEffectType, Integer> potionEffect : getPotionEffects()) {
            p.addPotionEffect(new PotionEffect(potionEffect.getFirst(), -1, potionEffect.getSecond(), false, false, false));
        }

        extendedPlayer.removeCompanion();
        extendedPlayer.spawnCompanion();
    }
}

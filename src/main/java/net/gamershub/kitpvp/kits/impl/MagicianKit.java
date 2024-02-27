package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MagicianKit extends Kit {
    public MagicianKit() {
        super("magician", "Magician");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        leatherHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(leatherHelmet, Color.RED);
        return leatherHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        leatherChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(leatherChestplate, Color.NAVY);
        return leatherChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(leatherLeggings, Color.NAVY);
        return leatherLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherBoots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        setLeatherArmorColor(leatherBoots, Color.BLACK);
        return leatherBoots;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        return new ItemStack[]{
                CustomItemHandler.MAGIC_WAND.build(1),
                new ItemStack(Material.BOOK),
                new ItemStack(Material.GOLDEN_APPLE, 3),
                new ItemStack(Material.GILDED_BLACKSTONE, 64),
                new ItemStack(Material.WATER_BUCKET),
        };
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        if (extendedPlayer.getSelectedKitId().equals(getId()) && extendedPlayer.getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            ItemStack slot = p.getInventory().getItem(e.getNewSlot());
            if (slot == null) return;

            if (slot.getType() == Material.BOOK) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 3, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, -1, 1, false, false, false));
                p.getInventory().setHelmet(new ItemStack(Material.AIR));
                p.getInventory().setChestplate(new ItemStack(Material.AIR));
                p.getInventory().setLeggings(new ItemStack(Material.AIR));
                p.getInventory().setBoots(new ItemStack(Material.AIR));
            }
            else {
                p.removePotionEffect(PotionEffectType.SPEED);
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                p.getInventory().setHelmet(getHeadContent());
                p.getInventory().setChestplate(getChestContent());
                p.getInventory().setLeggings(getLegsContent());
                p.getInventory().setBoots(getFeetContent());
            }
        }
    }
}

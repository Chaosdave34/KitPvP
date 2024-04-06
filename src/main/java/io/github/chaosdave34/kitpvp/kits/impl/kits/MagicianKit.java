package io.github.chaosdave34.kitpvp.kits.impl.kits;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import io.github.chaosdave34.kitpvp.kits.Kit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

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
        setLeatherArmorColor(leatherBoots, Color.BLACK);
        return leatherBoots;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        return new ItemStack[]{
                CustomItemHandler.MAGIC_WAND.build(),
                new ItemStack(Material.BOOK),
                new ItemStack(Material.WATER_BUCKET),
        };
    }

    @Override
    public ItemStack[] getKillRewards() {
        ItemStack splashPotion = new ItemStack(Material.SPLASH_POTION, 2);
        splashPotion.editMeta(PotionMeta.class, potionMeta -> potionMeta.setBasePotionType(PotionType.INSTANT_HEAL));

        return new ItemStack[]{
                splashPotion,
                new ItemStack(Material.GILDED_BLACKSTONE, 32),
        };
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        if (extendedPlayer.getSelectedKitId().equals(getId()) && extendedPlayer.getGameState() == ExtendedPlayer.GameState.KITS_IN_GAME) {
            ItemStack slot = p.getInventory().getItem(e.getNewSlot());

            // Todo: improve this
            if (slot == null || slot.getType() != Material.BOOK) {
                p.removePotionEffect(PotionEffectType.SPEED);
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                p.getInventory().setHelmet(getHeadContent());
                p.getInventory().setChestplate(getChestContent());
                p.getInventory().setLeggings(getLegsContent());
                p.getInventory().setBoots(getFeetContent());
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 3, false, false, false));
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, -1, 0, false, false, false));
                p.getInventory().setHelmet(new ItemStack(Material.AIR));
                p.getInventory().setChestplate(new ItemStack(Material.AIR));
                p.getInventory().setLeggings(new ItemStack(Material.AIR));
                p.getInventory().setBoots(new ItemStack(Material.AIR));
            }
        }
    }
}

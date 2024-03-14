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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class ArtilleryManKit extends Kit {
    public ArtilleryManKit() {
        super("artilleryman", "Artilleryman");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        leatherHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherHelmet.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
        setLeatherArmorColor(leatherHelmet, Color.NAVY);
        return leatherHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        return CustomItemHandler.JETPACK.build();
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
        setLeatherArmorColor(leatherLeggings, Color.NAVY);
        return leatherLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherBoots.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
        leatherBoots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
        setLeatherArmorColor(leatherBoots, Color.NAVY);
        return leatherBoots;
    }

    @Override
    public ItemStack getOffhandContent() {
        ItemStack rocket = new ItemStack(Material.FIREWORK_ROCKET, 3);
        rocket.editMeta(FireworkMeta.class, fireworkMeta -> fireworkMeta.setPower(5));
        return rocket;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        return new ItemStack[]{
                CustomItemHandler.ROCKET_LAUNCHER.build(),
                new ItemStack(Material.STONE_SWORD),
                new ItemStack(Material.WATER_BUCKET),
        };
    }

    @Override
    public ItemStack[] getKillRewards() {
        return new ItemStack[]{
                new ItemStack(Material.GOLDEN_APPLE),
                new ItemStack(Material.COBBLESTONE, 32),
        };
    }

    @EventHandler
    public void onRocketLaunch(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) {
           if (e.getMaterial() == Material.FIREWORK_ROCKET) {
               e.setCancelled(true);
           }
        }

    }
}

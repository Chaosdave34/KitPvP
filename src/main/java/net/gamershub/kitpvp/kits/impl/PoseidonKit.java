package net.gamershub.kitpvp.kits.impl;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.events.PlayerSpawnEvent;
import net.gamershub.kitpvp.items.CustomItemHandler;
import net.gamershub.kitpvp.kits.Kit;
import net.gamershub.kitpvp.kits.KitHandler;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PoseidonKit extends Kit {
    public PoseidonKit() {
        super("poseidon", "Poseidon");
    }

    @Override
    public ItemStack getHeadContent() {
        ItemStack turtleHelmet = new ItemStack(Material.TURTLE_HELMET);
        turtleHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        turtleHelmet.addEnchantment(Enchantment.OXYGEN, 3);
        turtleHelmet.addEnchantment(Enchantment.WATER_WORKER, 1);
        return turtleHelmet;
    }

    @Override
    public ItemStack getChestContent() {
        ItemStack leatherChestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        leatherChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(leatherChestplate, Color.AQUA);
        return leatherChestplate;
    }

    @Override
    public ItemStack getLegsContent() {
        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        setLeatherArmorColor(leatherLeggings, Color.AQUA);
        return leatherLeggings;
    }

    @Override
    public ItemStack getFeetContent() {
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        leatherBoots.addEnchantment(Enchantment.DEPTH_STRIDER, 3);
        setLeatherArmorColor(leatherBoots, Color.AQUA);
        return leatherBoots;
    }

    @Override
    public ItemStack[] getInventoryContent() {
        ItemStack riptideTrident = new ItemStack(Material.TRIDENT);
        riptideTrident.addUnsafeEnchantment(Enchantment.RIPTIDE, 5);

        return new ItemStack[]{
                CustomItemHandler.POSEIDONS_TRIDENT.build(),
                riptideTrident,
                new ItemStack(Material.WATER_BUCKET),
        };
    }

    @Override
    public ItemStack[] getKillRewards() {
        return new ItemStack[]{
                new ItemStack(Material.GOLDEN_APPLE),
                new ItemStack(Material.PRISMARINE, 32),
        };
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e) {
        removeTrident(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawnEvent(PlayerSpawnEvent e) {
        removeTrident(e.getPlayer());
    }


    private void removeTrident(Player p) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        if (extendedPlayer.getSelectedKit() == KitHandler.POSEIDON) {
            p.getWorld().getEntitiesByClass(Trident.class).forEach(trident -> {
                if (trident.getShooter() instanceof Player shooter)
                    if (p.getEntityId() == shooter.getEntityId())
                        trident.remove();
            });
        }
    }

}

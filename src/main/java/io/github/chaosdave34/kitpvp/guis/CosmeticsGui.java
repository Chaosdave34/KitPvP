package io.github.chaosdave34.kitpvp.guis;

import io.github.chaosdave34.ghutils.gui.Gui;
import io.github.chaosdave34.ghutils.gui.InventoryClickHandler;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CosmeticsGui extends Gui {
    public CosmeticsGui() {
        super(2, Component.text("Cosmetics"), true);
    }

    @Override
    public @NotNull Inventory build(Player p, Inventory inventory) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);

        String selectedProjectileTrail = extendedPlayer.getProjectileTrailId() == null ? "None" : KitPvp.INSTANCE.getCosmeticHandler().getProjectileTrails().get(extendedPlayer.getProjectileTrailId()).getName();
        ItemStack projectileTrailButton = createItemStack(Material.ARROW, "Projectile Trails", true, false);
        projectileTrailButton.lore(List.of(Component.text("Selected: " + selectedProjectileTrail, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)));
        inventory.setItem(2, projectileTrailButton);

        String selectedKillEffect = extendedPlayer.getKillEffectId() == null ? "None" : KitPvp.INSTANCE.getCosmeticHandler().getKillEffects().get(extendedPlayer.getKillEffectId()).getName();
        ItemStack killEffectButton = createItemStack(Material.IRON_SWORD, "Kill Effects", true, false);
        killEffectButton.lore(List.of(Component.text("Selected: " + selectedKillEffect, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)));
        inventory.setItem(6, killEffectButton);

        inventory.setItem(13, createItemStack(Material.BARRIER, "Close", true, false));

        fillEmpty(inventory, Material.GRAY_STAINED_GLASS_PANE);

        return inventory;
    }

    @InventoryClickHandler(slot = 2)
    public void onProjectileTrails(InventoryClickEvent e) {
        Guis.PROJECTILE_TRAILS.show((Player) e.getWhoClicked());
    }

    @InventoryClickHandler(slot = 6)
    public void onKillEffects(InventoryClickEvent e) {
        Guis.KILL_EFFECTS.show((Player) e.getWhoClicked());
    }

    @InventoryClickHandler(slot = 13)
    public void onCloseButton(InventoryClickEvent e) {
        e.getInventory().close();
    }
}

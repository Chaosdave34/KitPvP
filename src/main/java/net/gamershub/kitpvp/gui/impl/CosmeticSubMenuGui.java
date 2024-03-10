package net.gamershub.kitpvp.gui.impl;

import lombok.NonNull;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.cosmetics.Cosmetic;
import net.gamershub.kitpvp.gui.Gui;
import net.gamershub.kitpvp.gui.GuiHandler;
import net.gamershub.kitpvp.gui.InventoryClickHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class CosmeticSubMenuGui extends Gui {
    private final String name;
    private final Collection<? extends Cosmetic> cosmetics;

    public CosmeticSubMenuGui(String name, Collection<? extends Cosmetic> cosmetics, int rows) {
        super(rows, Component.text(name), true);
        this.name = name;
        this.cosmetics = cosmetics;
    }

    @Override
    protected @NonNull Inventory build(Player p, Inventory inventory) {
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);


        Component noneDisplayName = Component.text("None").decoration(TextDecoration.ITALIC, false);
        TextColor textColor = NamedTextColor.WHITE;
        boolean glint = false;
        if ((this.name.equals("Projectile Trails") && extendedPlayer.getProjectileTrailId() == null) || (this.name.equals("Kill Effects") && extendedPlayer.getKillEffectId() == null)) {
            textColor = NamedTextColor.GREEN;
            glint = true;
        }
        noneDisplayName = noneDisplayName.color(textColor);
        ItemStack none = createItemStack(Material.BARRIER, noneDisplayName, true, glint);
        inventory.setItem(0, none);

        int slot = 1;
        for (Cosmetic cosmetic : cosmetics.stream().sorted(Comparator.comparingInt(Cosmetic::getLevelRequirement)).toList()) {
            if (slot < (rows - 1) * 9) {
                Component component = Component.text(cosmetic.getName(), NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);

                String selected = null;
                if (this.name.equals("Projectile Trails"))
                    selected = extendedPlayer.getProjectileTrailId();
                else if (this.name.equals("Kill Effects"))
                    selected = extendedPlayer.getKillEffectId();

                glint = false;
                if (cosmetic.getId().equals(selected)) {
                    component = component.color(NamedTextColor.GREEN);
                    glint = true;
                }

                if (extendedPlayer.getLevel() < cosmetic.getLevelRequirement())
                    component = component.color(NamedTextColor.RED);

                ItemStack itemStack = createItemStack(cosmetic.getIcon(), component, true, glint);

                if (extendedPlayer.getLevel() < cosmetic.getLevelRequirement())
                    itemStack.lore(List.of(Component.text("Unlocked at level " + cosmetic.getLevelRequirement() + ".", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)));

                inventory.setItem(slot, itemStack);
            } else break;

            slot++;
        }

        inventory.setItem(rows * 9 - 6, createItemStack(Material.ARROW, "Back", true, false));
        inventory.setItem(rows * 9 - 5, createItemStack(Material.BARRIER, "Close", true, false));
        return inventory;
    }

    @InventoryClickHandler(slot = 0)
    public void onNoneButton(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        if (name.equals("Projectile Trails"))
            extendedPlayer.setProjectileTrailId(null);

        else if (name.equals("Kill Effects"))
            extendedPlayer.setKillEffectId(null);

        show(p);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getRawSlot() == rows * 9 - 6)
            GuiHandler.COSMETICS.show((Player) e.getWhoClicked());
        else if (e.getRawSlot() == rows * 9 - 5)
            e.getInventory().close();


        super.onInventoryClick(e);
        if (e.getClickedInventory() == null) return;

        Player p = (Player) e.getWhoClicked();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        if (e.getRawSlot() - 1 >= cosmetics.size() || cosmetics.isEmpty() || e.getRawSlot() == 0) return;
        Cosmetic cosmetic = cosmetics.stream().sorted(Comparator.comparingInt(Cosmetic::getLevelRequirement)).toList().get(e.getRawSlot() - 1);

        if (extendedPlayer.getLevel() < cosmetic.getLevelRequirement() || cosmetic.getId().equals(extendedPlayer.getProjectileTrailId()))
            return;

        if (name.equals("Projectile Trails"))
            extendedPlayer.setProjectileTrailId(cosmetic.getId());

        else if (name.equals("Kill Effects"))
            extendedPlayer.setKillEffectId(cosmetic.getId());

        show(p);
    }
}

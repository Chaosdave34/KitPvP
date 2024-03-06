package net.gamershub.kitpvp.gui.impl;

import lombok.NonNull;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.cosmetics.Cosmetic;
import net.gamershub.kitpvp.enchantments.CustomEnchantmentHandler;
import net.gamershub.kitpvp.gui.Gui;
import net.gamershub.kitpvp.gui.GuiHandler;
import net.gamershub.kitpvp.gui.InventoryClickHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

    public CosmeticSubMenuGui(String name, Collection<? extends Cosmetic> cosmetics) {
        super(3, Component.text(name), true);
        this.name = name;
        this.cosmetics = cosmetics;
    }

    @Override
    protected @NonNull Inventory build(Player p, Inventory inventory) {
        int slot = 0;
        for (Cosmetic cosmetic : cosmetics.stream().sorted(Comparator.comparingInt(Cosmetic::getLevelRequirement)).toList()) {
            if (slot < (rows - 2) * 9) {
                ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
                Component component = Component.text(cosmetic.getName(), NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);

                String selected = null;
                if (this.name.equals("Projectile Trails"))
                    selected = extendedPlayer.getProjectileTrailId();
                else if (this.name.equals("Kill Effects"))
                    selected = extendedPlayer.getKillEffectId();

                if (cosmetic.getId().equals(selected))
                    component = component.color(NamedTextColor.GREEN);

                if (extendedPlayer.getLevel() < cosmetic.getLevelRequirement())
                    component = component.color(NamedTextColor.RED);

                ItemStack itemStack = createItemStack(cosmetic.getIcon(), component, true);

                if (cosmetic.getId().equals(selected))
                    itemStack.addUnsafeEnchantment(CustomEnchantmentHandler.BACKSTAB, 1); // Hacky way to add enchantment glint

                if (extendedPlayer.getLevel() < cosmetic.getLevelRequirement())
                    itemStack.lore(List.of(Component.text("Unlocked at level " + cosmetic.getLevelRequirement() + ".", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)));


                inventory.setItem(slot, itemStack);
            } else break;

            slot++;
        }

        inventory.setItem(21, createItemStack(Material.ARROW, "Back", true));
        inventory.setItem(22, createItemStack(Material.BARRIER, "Close", true));
        return inventory;
    }

    @InventoryClickHandler(slot = 21)
    public void onBackButton(InventoryClickEvent e) {
        GuiHandler.COSMETICS.show((Player) e.getWhoClicked());
    }

    @InventoryClickHandler(slot = 22)
    public void onCloseButton(InventoryClickEvent e) {
        e.getInventory().close();
    }


    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        super.onInventoryClick(e);
        if (e.getClickedInventory() == null) return;

        Player p = (Player) e.getWhoClicked();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        if (e.getRawSlot() > cosmetics.size() || cosmetics.isEmpty()) return;
        Cosmetic cosmetic = cosmetics.stream().sorted(Comparator.comparingInt(Cosmetic::getLevelRequirement)).toList().get(e.getRawSlot());

        if (extendedPlayer.getLevel() < cosmetic.getLevelRequirement() || cosmetic.getId().equals(extendedPlayer.getProjectileTrailId()))
            return;

        if (name.equals("Projectile Trails"))
            extendedPlayer.setProjectileTrailId(cosmetic.getId());

        else if (name.equals("Kill Effects"))
            extendedPlayer.setKillEffectId(cosmetic.getId());

        show(p);
    }
}

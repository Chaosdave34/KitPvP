package io.github.chaosdave34.kitpvp.guis;

import io.github.chaosdave34.ghutils.gui.Gui;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.kits.ElytraKit;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ElytraKitsGui extends Gui {
    private List<ElytraKit> kits;

    public ElytraKitsGui() {
        super(KitPvp.INSTANCE.getElytraKitHandler().getKits().size() / 9 + 2, Component.text("Kits"), true);

        kits = KitPvp.INSTANCE.getElytraKitHandler().getKits().values().stream().toList();
    }

    @Override
    protected @NonNull Inventory build(Player p, Inventory inventory) {
        inventory.setItem(rows * 9 - 5, createItemStack(Material.BARRIER, "Close", true, false));

        int slot = 0;
        for (ElytraKit elytraKit : kits) {
            if (slot < (rows) * 9) {
                Component component = Component.text(elytraKit.getName(), NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);

                boolean glint = false;
                if (elytraKit.getId().equals(ExtendedPlayer.from(p).getSelectedElytraKitId())) {
                    component = component.color(NamedTextColor.GREEN);
                    glint = true;
                }

                ItemStack itemStack = createItemStack(elytraKit.getIcon(), component, true, glint);

                inventory.setItem(slot, itemStack);
            } else break;

            slot++;
        }

        return inventory;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        super.onInventoryClick(e);

        if (e.getRawSlot() == rows * 9 - 5)
            e.getInventory().close();

        Player p = (Player) e.getWhoClicked();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);


        if (e.getRawSlot() - 1 >= kits.size() || kits.isEmpty()) return;

        ElytraKit kit = kits.get(e.getRawSlot());

        if (!extendedPlayer.getSelectedElytraKitId().equals(kit.getId())) {
            kit.apply(p);

            p.sendMessage(Component.text("You have selected the Kit ", NamedTextColor.GRAY)
                    .append(Component.text(kit.getName(), NamedTextColor.GREEN))
                    .append(Component.text(".", NamedTextColor.GRAY)));

            e.getInventory().close();
        }
    }
}

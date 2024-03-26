package io.github.chaosdave34.kitpvp.guis;

import io.github.chaosdave34.ghutils.gui.Gui;
import io.github.chaosdave34.ghutils.gui.InventoryClickHandler;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.kits.ElytraKit;
import io.github.chaosdave34.kitpvp.kits.ElytraKitHandler;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ElytraKitsGui extends Gui {
    private final Map<Integer, ElytraKit> kits = new HashMap<>();

    public ElytraKitsGui() {
        super(4, Component.text("Kits"), true);
    }

    @Override
    protected @NonNull Inventory build(Player p, Inventory inventory) {
        inventory.setItem(31, createItemStack(Material.BARRIER, "Close", true, false));

        setIcon(11, inventory, p, ElytraKitHandler.KNIGHT);
        setIcon(12, inventory, p, ElytraKitHandler.SNIPER);
        setIcon(13, inventory, p, ElytraKitHandler.PYRO);
        setIcon(14, inventory, p, ElytraKitHandler.TANK);
        setIcon(15, inventory, p, ElytraKitHandler.KNOCKER);
        setIcon(20, inventory, p, ElytraKitHandler.ROCKET_LAUNCHER);
        setIcon(21, inventory, p, ElytraKitHandler.POSEIDON);
        setIcon(22, inventory, p, ElytraKitHandler.TELEPORTER);
        setIcon(23, inventory, p, ElytraKitHandler.HEALER);
        setIcon(24, inventory, p, ElytraKitHandler.CHEMIST);

        fillEmpty(inventory, Material.GRAY_STAINED_GLASS_PANE);

        return inventory;
    }

    private void setIcon(int slot, Inventory inventory, Player player, ElytraKit elytraKit) {
        Component component = Component.text(elytraKit.getName(), NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);

        boolean glint = false;
        if (elytraKit.getId().equals(ExtendedPlayer.from(player).getSelectedElytraKitId())) {
            component = component.color(NamedTextColor.GREEN);
            glint = true;
        }

        ItemStack itemStack = createItemStack(elytraKit.getIcon(), component, true, glint);

        inventory.setItem(slot, itemStack);

        kits.put(slot, elytraKit);
    }

    @InventoryClickHandler(slot = 31)
    public void onClose(InventoryClickEvent e) {
        e.getInventory().close();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        super.onInventoryClick(e);

        Player p = (Player) e.getWhoClicked();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        int slot = e.getRawSlot();

        if (!kits.containsKey(slot)) return;

        ElytraKit kit = kits.get(slot);

        if (!extendedPlayer.getSelectedElytraKitId().equals(kit.getId())) {
            kit.apply(p);

            p.sendMessage(Component.text("You have selected the Kit ", NamedTextColor.GRAY)
                    .append(Component.text(kit.getName(), NamedTextColor.GREEN))
                    .append(Component.text(".", NamedTextColor.GRAY)));

            e.getInventory().close();
        }
    }
}

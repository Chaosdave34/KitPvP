package net.gamershub.kitpvp.guis;

import lombok.Getter;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.guis.impl.CosmeticSubMenuGui;
import net.gamershub.kitpvp.guis.impl.CosmeticsGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class GuiHandler implements Listener {
    private final Map<UUID, Gui> openGuis = new HashMap<>();

    public static Gui COSMETICS;
    public static Gui PROJECTILE_TRAILS;
    public static Gui KILL_EFFECTS;

    public GuiHandler() {
        COSMETICS = new CosmeticsGui();
        PROJECTILE_TRAILS = new CosmeticSubMenuGui("Projectile Trails", KitPvpPlugin.INSTANCE.getCosmeticHandler().getProjectileTrails().values(), 3);
        KILL_EFFECTS = new CosmeticSubMenuGui("Kill Effects", KitPvpPlugin.INSTANCE.getCosmeticHandler().getKillEffects().values(), 2);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (openGuis.containsKey(e.getPlayer().getUniqueId())) {
            openGuis.get(e.getPlayer().getUniqueId()).onInventoryClose(e);
        }
        openGuis.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (openGuis.containsKey(e.getWhoClicked().getUniqueId())) {
            openGuis.get(e.getWhoClicked().getUniqueId()).onInventoryClick(e);
        }
    }
}

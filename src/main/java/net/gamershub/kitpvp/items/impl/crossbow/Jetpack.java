package net.gamershub.kitpvp.items.impl.crossbow;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.items.CustomItem;
import net.gamershub.kitpvp.kits.KitHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class Jetpack extends CustomItem {
    public Jetpack() {
        super(Material.LEATHER_CHESTPLATE, "jetpack", false);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Jetpack");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.IN_GAME) {
            if (extendedPlayer.getSelectedKit() == KitHandler.CROSSBOW) {
                if (p.isSneaking()) {

                    p.setVelocity(p.getVelocity().setY(0.1));

                }

            }
        }
    }

}

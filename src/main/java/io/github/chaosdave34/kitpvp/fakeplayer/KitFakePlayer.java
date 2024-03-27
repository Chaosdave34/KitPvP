package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghutils.fakeplayer.FakePlayer;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class KitFakePlayer extends FakePlayer {
    public KitFakePlayer() {
        super("KitPvP", "world_elytra", new Location(null ,16.5, 200, -2.5), 10, 0, true);
    }

    @Override
    public void onAttack(@NotNull Player p) {
        ExtendedPlayer.from(p).spawn(ExtendedPlayer.GameType.NORMAL);
    }

    @Override
    public void onInteract(@NotNull Player p, @NotNull EquipmentSlot hand) {
        ExtendedPlayer.from(p).spawn(ExtendedPlayer.GameType.NORMAL);
    }
}

package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghutils.fakeplayer.FakePlayer;
import io.github.chaosdave34.kitpvp.guis.Guis;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ElytraKitSelectorFakePlayer extends FakePlayer {
    public ElytraKitSelectorFakePlayer() {
        super("Kits", "world_elytra", new Location(null, 10.5, 200, -5.5), 0, 0, true);
    }

    @Override
    public void onAttack(@NotNull Player p) {
        Guis.ELYTRA_KITS.show(p);
    }

    @Override
    public void onActualInteract(@NotNull Player p) {
        Guis.ELYTRA_KITS.show(p);
    }
}

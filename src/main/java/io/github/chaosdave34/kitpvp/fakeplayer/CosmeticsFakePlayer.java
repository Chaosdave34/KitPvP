package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghutils.fakeplayer.FakePlayer;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.guis.Guis;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CosmeticsFakePlayer extends FakePlayer {
    public CosmeticsFakePlayer(String world, Location location) {
        super("Cosmetics", world, location, location.getYaw(), location.getPitch(), true);
    }

    @Override
    public void onAttack(@NotNull Player p) {
        onClick(p);
    }

    @Override
    public void onActualInteract(@NotNull Player p) {
        onClick(p);
    }

    private void onClick(Player p) {
        if (ExtendedPlayer.from(p).inSpawn()) {
            Guis.COSMETICS.show(p);
        }
    }
}

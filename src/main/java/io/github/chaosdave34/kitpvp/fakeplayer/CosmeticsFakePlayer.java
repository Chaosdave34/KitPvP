package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghutils.fakeplayer.FakePlayer;
import io.github.chaosdave34.kitpvp.guis.Guis;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CosmeticsFakePlayer extends FakePlayer {
    public CosmeticsFakePlayer(String world, Location location) {
        super("Cosmetics", world, location, location.getYaw(), location.getPitch(), true);
    }

    @Override
    public void onAttack(Player p) {
        Guis.COSMETICS.show(p);
    }
}

package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghutils.fakeplayer.FakePlayer;
import io.github.chaosdave34.kitpvp.guis.Guis;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CosmeticsFakePlayer extends FakePlayer {
    public CosmeticsFakePlayer() {
        super("Cosmetics", "world", new Location(null, 4.5,120,12.5), 180, 0, true);
    }

    @Override
    public void onAttack(Player p) {
        Guis.COSMETICS.show(p);
    }
}

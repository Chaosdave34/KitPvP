package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghutils.fakeplayer.FakePlayer;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ReturnFakePlayer extends FakePlayer {
    public ReturnFakePlayer() {
        super("Return", "world_elytra", new Location(null ,16.5, 200, -2.5), 10, 0, true);
    }

    @Override
    public void onAttack(Player p) {
        ExtendedPlayer.from(p).spawn(ExtendedPlayer.GameType.NORMAL);
    }
}

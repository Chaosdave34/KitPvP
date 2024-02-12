package net.gamershub.kitpvp;

import lombok.Getter;
import lombok.Setter;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class ExtendedPlayer {
    private final UUID uuid;
    private GameState gameState;
    private Kit selectedKit;

    public ExtendedPlayer(Player p) {
        uuid = p.getUniqueId();
        gameState = GameState.LOBBY;
    }

    public enum GameState {
        LOBBY,
        IN_GAME
    }
}

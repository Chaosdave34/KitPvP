package net.gamershub.kitpvp;

import lombok.Getter;
import lombok.Setter;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class ExtendedPlayer {
    private transient final UUID uuid;
    private GameState gameState;
    private String selectedKit;

    public ExtendedPlayer(Player p) {
        uuid = p.getUniqueId();
        gameState = GameState.LOBBY;
    }

    public Kit getSelectedKit() {
        return selectedKit == null ? null : KitPvpPlugin.INSTANCE.getKitHandler().getKits().get(selectedKit);
    }

    public enum GameState {
        LOBBY,
        IN_GAME
    }
}

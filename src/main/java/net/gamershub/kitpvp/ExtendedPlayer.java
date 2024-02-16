package net.gamershub.kitpvp;

import lombok.Getter;
import lombok.Setter;
import net.gamershub.kitpvp.kits.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

@Getter
@Setter
public class ExtendedPlayer {
    private final UUID uuid;
    private String selectedKitId;

    private transient GameState gameState;
    private transient Scoreboard scoreboard;

    private transient int killSteak;
    private int totalKills;
    private int totalDeaths;

    public ExtendedPlayer(Player p) {
        uuid = p.getUniqueId();
        gameState = GameState.SPAWN;
    }

    public Kit getSelectedKit() {
        return selectedKitId == null ? null : KitPvpPlugin.INSTANCE.getKitHandler().getKits().get(selectedKitId);
    }

    public void spawnPlayer() {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) return;

        p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100.5, -8.5, 0, 0));

        AttributeInstance maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null)
            p.setHealth(maxHealth.getValue());

        p.setFoodLevel(20);
        p.setSaturation(5);
        p.setExhaustion(0);

        p.setFireTicks(0);

        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }

        gameState = GameState.SPAWN;

        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("default", Criteria.DUMMY, Component.text("KitPvP", NamedTextColor.YELLOW, TextDecoration.BOLD));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            p.setScoreboard(scoreboard);

            updateScoreboardLines();
        }
    }

    public void updateScoreboardLines() {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) return;

        Objective objective = scoreboard.getObjective("default");
        if (objective == null) return;

        scoreboard.resetScores("*");

        objective.getScore("test1").setScore(3);
        objective.getScore("test2").setScore(2);
        objective.getScore("test3").setScore(1);
        objective.getScore("test4").setScore(0);
    }

    public void incrementKillStreak() {
        killSteak++;
    }

    public void incrementTotalKills() {
        totalKills++;
    }

    public void incrementTotalDeaths() {
        totalDeaths++;
    }

    public enum GameState {
        SPAWN,
        IN_GAME,
        DEBUG
    }
}

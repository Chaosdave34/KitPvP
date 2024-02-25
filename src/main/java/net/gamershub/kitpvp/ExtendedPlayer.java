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

    private int experiencePoints;

    public ExtendedPlayer(Player p) {
        uuid = p.getUniqueId();
        gameState = GameState.SPAWN;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;

        updateScoreboardLines();
    }

    public Kit getSelectedKit() {
        return selectedKitId == null ? null : KitPvpPlugin.INSTANCE.getKitHandler().getKits().get(selectedKitId);
    }

    public void spawnPlayer() {
        Player p = getPlayer();
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
        }

        updateScoreboardLines();
    }

    public void updateDisplayName() {
        Player p = getPlayer();
        p.displayName(Component.text("[")
                .append(Component.text(getLevel()))
                .append(Component.text("] "))
                .append(p.name()));

        p.playerListName(p.displayName());
    }

    public void updateScoreboardLines() {
        Player p = getPlayer();
        if (p == null) return;

        Objective objective = scoreboard.getObjective("default");
        if (objective == null) return;

        scoreboard.getEntries().forEach((entry) -> scoreboard.resetScores(entry));

        objective.getScore("Level: " + getLevel()).setScore(5);
        objective.getScore("Missing XP: " + getMissingExperience()).setScore(4);
        objective.getScore("  ").setScore(3);
        objective.getScore("Kill Streak: " + killSteak).setScore(2);
        objective.getScore("   ").setScore(1);
        objective.getScore("Status: " + gameState.displayName).setScore(0);
    }

    public void incrementKillStreak() {
        killSteak++;

        if (killSteak % 5 == 0)
            Bukkit.broadcast(getPlayer().displayName().append(Component.text(" has reached a kill streak of " + killSteak / 5)));

        updateScoreboardLines();
    }

    public void incrementTotalKills() {
        totalKills++;
    }

    public void incrementTotalDeaths() {
        totalDeaths++;
    }

    public void addExperiencePoints(int amount) {
        int oldLevel = getLevel();

        experiencePoints += amount;
        updateScoreboardLines();

        if (getLevel() > oldLevel) {
            updateDisplayName();
            getPlayer().sendMessage(Component.text("You have reached Level " + getLevel() + "."));
        }
    }

    public int getLevel() {
        int x = experiencePoints;
        int i = 1;

        while (x >= 20 + (i - 1) * 5) {
            x -= 20 + (i - 1) * 5;
            i++;
        }

        return i;
    }

    public int getMissingExperience() {
        return getRequiredExperienceUntilLevel(getLevel() + 1) - experiencePoints;
    }

    public int getRequiredExperienceUntilLevel(int level) {
        if (level == 1) return 0;
        if (level == 2) return 20;
        level -= 1;
        return level * 20 + (Utils.binomialCoefficient(level, 2) * 5);
    }

    public enum GameState {
        SPAWN("Idle"),
        IN_GAME("Active"),
        DEBUG("Debug");

        private final String displayName;

        GameState(String name) {this.displayName = name;}
    }
}

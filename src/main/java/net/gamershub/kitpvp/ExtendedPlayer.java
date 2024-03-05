package net.gamershub.kitpvp;

import lombok.Getter;
import lombok.Setter;
import net.gamershub.kitpvp.events.PlayerSpawnEvent;
import net.gamershub.kitpvp.kits.Kit;
import net.gamershub.kitpvp.kits.KitHandler;
import net.gamershub.kitpvp.textdisplay.TextDisplayHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class ExtendedPlayer {
    private final UUID uuid;
    private String selectedKitId;

    private transient GameState gameState;
    private transient Scoreboard scoreboard;
    private transient int combatCooldown;

    private transient Entity morph;

    private transient int killSteak;
    private int highestKillStreak;
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

    public void setSelectedKitId(String id) {
        selectedKitId = id;
        updateScoreboardLines();
    }

    public Kit getSelectedKit() {
        return selectedKitId == null ? null : KitPvpPlugin.INSTANCE.getKitHandler().getKits().get(selectedKitId);
    }

    public void spawnPlayer() {
        Player p = getPlayer();
        if (p == null) return;

        unmorph();

        p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100.5, -8.5, 0, 0));

        p.setFoodLevel(20);
        p.setSaturation(5);
        p.setExhaustion(0);

        p.setFireTicks(0);
        p.setFreezeTicks(0);

        p.clearActiveItem();

        killSteak = 0;
        combatCooldown = 0;

        gameState = GameState.SPAWN;

        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("default", Criteria.DUMMY, Component.text("KitPvP", NamedTextColor.YELLOW, TextDecoration.BOLD));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            p.setScoreboard(scoreboard);
        }

        updateScoreboardLines();

        if (getSelectedKit() == null) setSelectedKitId(KitHandler.CLASSIC.getId());

        getSelectedKit().apply(p);

        new PlayerSpawnEvent(p).callEvent();
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

        objective.getScore("Level: " + getLevel()).setScore(6);
        objective.getScore("Missing XP: §b" + getMissingExperience()).setScore(5);
        objective.getScore("  ").setScore(4);
        String kitName = getSelectedKit() == null ? "None" : getSelectedKit().getName();
        objective.getScore("Kit: " + kitName).setScore(3);
        objective.getScore("Kill Streak: " + killSteak).setScore(2);
        objective.getScore("   ").setScore(1);
        objective.getScore("Status: " + (combatCooldown > 0 ? "§cFighting" : getGameState().displayName)).setScore(0);
    }

    public void incrementKillStreak() {
        killSteak++;

        if (killSteak % 5 == 0)
            Bukkit.broadcast(getPlayer().displayName().append(Component.text(" has reached a kill streak of " + killSteak)));

        if (killSteak > highestKillStreak) {
            highestKillStreak = killSteak;
            KitPvpPlugin.INSTANCE.getTextDisplayHandler().updateTextDisplay(getPlayer(), TextDisplayHandler.PERSONAL_STATISTICS);

            Map<UUID, Integer> highestKillstreaks = KitPvpPlugin.INSTANCE.getHighestKillstreaks();


            if (highestKillstreaks.size() < 5 || getLevel() > Collections.min(highestKillstreaks.values())) {

                if (highestKillstreaks.size() == 5 && !highestKillstreaks.containsKey(uuid)) {
                    for (UUID key : highestKillstreaks.keySet()) {
                        if (Objects.equals(highestKillstreaks.get(key), Collections.min(highestKillstreaks.values()))) {
                            highestKillstreaks.remove(key);
                            break;
                        }
                    }
                }

                highestKillstreaks.put(uuid, getLevel());

                KitPvpPlugin.INSTANCE.getTextDisplayHandler().updateTextDisplayForAll(TextDisplayHandler.HIGHEST_KILLSTREAKS);

            }
        }

        updateScoreboardLines();
    }

    public void incrementTotalKills() {
        totalKills++;
        KitPvpPlugin.INSTANCE.getTextDisplayHandler().updateTextDisplay(getPlayer(), TextDisplayHandler.PERSONAL_STATISTICS);
    }

    public void incrementTotalDeaths() {
        totalDeaths++;
        KitPvpPlugin.INSTANCE.getTextDisplayHandler().updateTextDisplay(getPlayer(), TextDisplayHandler.PERSONAL_STATISTICS);
    }

    public void addExperiencePoints(int amount) {
        int oldLevel = getLevel();

        experiencePoints += amount;
        updateScoreboardLines();

        if (getLevel() > oldLevel) {
            updateDisplayName();
            getPlayer().sendMessage(Component.text("You have reached Level " + getLevel() + "."));

            Map<UUID, Integer> highestLevels = KitPvpPlugin.INSTANCE.getHighestLevels();


            if (highestLevels.size() < 5 || getLevel() > Collections.min(highestLevels.values())) {

                if (highestLevels.size() == 5 && !highestLevels.containsKey(uuid)) {
                    for (UUID key : highestLevels.keySet()) {
                        if (Objects.equals(highestLevels.get(key), Collections.min(highestLevels.values()))) {
                            highestLevels.remove(key);
                            break;
                        }
                    }
                }

                highestLevels.put(uuid, getLevel());

                KitPvpPlugin.INSTANCE.getTextDisplayHandler().updateTextDisplayForAll(TextDisplayHandler.HIGHEST_LEVELS);

            }
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

    public void morph(EntityType entityType) {
        Player p = getPlayer();
        p.setGameMode(GameMode.SPECTATOR);
        this.morph = p.getWorld().spawnEntity(p.getLocation(), entityType, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
            entity.setMetadata("morph", new FixedMetadataValue(KitPvpPlugin.INSTANCE, p.getUniqueId()));
            entity.addPassenger(p);
            entity.setInvulnerable(true);
        });
    }

    public void unmorph() {
        Player p = getPlayer();
        if (morph != null) {
            p.setGameMode(GameMode.SURVIVAL);
            morph.removePassenger(p);
            morph.remove();
            morph = null;
        }
    }

    public void enterCombat() {
        if (combatCooldown == 0) {
            combatCooldown = 5;
            updateScoreboardLines();

            new BukkitRunnable() {
                @Override
                public void run() {
                    combatCooldown--;

                    if (combatCooldown == 0) {
                        this.cancel();
                        updateScoreboardLines();
                    }
                }
            }.runTaskTimer(KitPvpPlugin.INSTANCE, 0, 20);
        }

        combatCooldown = 5;
    }

    public enum GameState {
        SPAWN("§aIdle"),
        IN_GAME("§eActive"),
        DEBUG("§0Debug");

        private final String displayName;

        GameState(String name) {
            this.displayName = name;
        }
    }
}

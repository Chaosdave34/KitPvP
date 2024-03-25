package io.github.chaosdave34.kitpvp;

import io.github.chaosdave34.ghutils.utils.MathUtils;
import lombok.Getter;
import lombok.Setter;
import io.github.chaosdave34.ghutils.GHUtils;
;
import io.github.chaosdave34.kitpvp.challenges.Challenge;
import io.github.chaosdave34.kitpvp.companions.Companion;
import io.github.chaosdave34.kitpvp.customevents.CustomEventHandler;
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent;
import io.github.chaosdave34.kitpvp.kits.Kit;
import io.github.chaosdave34.kitpvp.kits.KitHandler;
import io.github.chaosdave34.kitpvp.textdisplays.TextDisplays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

@Getter
@Setter
public class ExtendedPlayer {
    private final UUID uuid;
    private String selectedKitId;

    private transient GameState gameState;
    private transient Scoreboard scoreboard;
    private transient int combatCooldown;

    private transient Entity morph;
    private transient Mob companion;

    private transient int killSteak;
    private int highestKillStreak;
    private int totalKills;
    private int totalDeaths;

    private int experiencePoints;

    private String projectileTrailId;
    private String killEffectId;

    private List<String> dailyChallenges;

    public ExtendedPlayer(Player p) {
        uuid = p.getUniqueId();
        gameState = GameState.SPAWN;
    }

    public static ExtendedPlayer from(UUID uuid) {
        return from(Bukkit.getPlayer(uuid));
    }

    public static ExtendedPlayer from(Player player) {
        return KitPvp.INSTANCE.getExtendedPlayer(player);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        updateScoreboardLines();
    }

    public boolean inSpawn() {
        return gameState == GameState.SPAWN;
    }

    public boolean inGame() {
        return gameState == GameState.IN_GAME;
    }

    public void setSelectedKitId(String id) {
        selectedKitId = id;
        updateScoreboardLines();
    }

    public Kit getSelectedKit() {
        return selectedKitId == null ? null : KitPvp.INSTANCE.getKitHandler().getKits().get(selectedKitId);
    }

    public void spawnPlayer() {
        Player p = getPlayer();
        if (p == null) return;

        unmorph();
        removeCompanion();

        p.teleport(new Location(Bukkit.getWorld("world"), 2.0, 120.0, 10.0, 180, 0));

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

    public void updatePlayerListFooter() {
        Component footer = Component.newline()
                .append(Component.text("Daily Challenges:", NamedTextColor.GREEN));

        for (String challengeId : dailyChallenges) {
            Challenge challenge = KitPvp.INSTANCE.getChallengesHandler().getChallenge(challengeId);
            TextColor textColor = NamedTextColor.WHITE;
            if (challenge.getProgress(getPlayer()) == challenge.getAmount())
                textColor = NamedTextColor.GREEN;

            footer = footer
                    .append(Component.newline())
                    .append(Component.text(challenge.getName() + " " + challenge.getProgress(getPlayer()) + "/" + challenge.getAmount(), textColor));
        }

        getPlayer().sendPlayerListFooter(footer);
    }

    public void updateDailyChallenges() {
        dailyChallenges = KitPvp.INSTANCE.getChallengesHandler().getThreeRandomChallenges().stream().map(Challenge::getId).toList();
        Bukkit.getScheduler().runTaskLater(KitPvp.INSTANCE, () -> getPlayer().sendMessage(Component.text("You have new daily challenges!")), 1);

        KitPvp.INSTANCE.getChallengesHandler().resetProgress(getPlayer());
    }

    public void incrementKillStreak() {
        killSteak++;

        if (killSteak % 5 == 0)
            Bukkit.broadcast(getPlayer().displayName().append(Component.text(" has reached a kill streak of " + killSteak)));

        if (killSteak > highestKillStreak) {
            highestKillStreak = killSteak;
            GHUtils.getTextDisplayHandler().updateTextDisplay(getPlayer(), TextDisplays.PERSONAL_STATISTICS);

            Map<UUID, Integer> highestKillstreaks = KitPvp.INSTANCE.getHighestKillstreaks();


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

                GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_KILLSTREAKS);

            }
        }

        updateScoreboardLines();
    }

    public void incrementTotalKills() {
        totalKills++;
        GHUtils.getTextDisplayHandler().updateTextDisplay(getPlayer(), TextDisplays.PERSONAL_STATISTICS);
    }

    public void incrementTotalDeaths() {
        totalDeaths++;
        GHUtils.getTextDisplayHandler().updateTextDisplay(getPlayer(), TextDisplays.PERSONAL_STATISTICS);
    }

    public void addExperiencePoints(int amount) {
        int oldLevel = getLevel();

        experiencePoints += amount;
        updateScoreboardLines();

        if (getLevel() > oldLevel) {
            updateDisplayName();
            getPlayer().sendMessage(Component.text("You have reached Level " + getLevel() + "."));

            Map<UUID, Integer> highestLevels = KitPvp.INSTANCE.getHighestLevels();


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

                GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_LEVELS);

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
        return level * 20 + (MathUtils.binomialCoefficient(level, 2) * 5);
    }

    public void morph(EntityType entityType) {
        Player p = getPlayer();
        p.setGameMode(GameMode.SPECTATOR);
        this.morph = p.getWorld().spawnEntity(p.getLocation(), entityType, CreatureSpawnEvent.SpawnReason.CUSTOM, (entity) -> {
            entity.setMetadata("morph", new FixedMetadataValue(KitPvp.INSTANCE, p.getUniqueId()));
            entity.addPassenger(p);
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

    public void spawnCompanion() {
        Companion comp = getSelectedKit().getCompanion();
        if (comp == null) {
            if (this.companion != null) removeCompanion();
            return;
        }

        if (this.companion == null) {
            this.companion = comp.createCompanion(getPlayer());
            getPlayer().getWorld().addEntity(companion);
        }
    }

    public void removeCompanion() {
        if (this.companion != null) {
            companion.remove();
            this.companion = null;
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

                    if (combatCooldown <= 0) {
                        combatCooldown = 0;
                        this.cancel();
                        updateScoreboardLines();
                    }
                }
            }.runTaskTimer(KitPvp.INSTANCE, 0, 20);
        }

        combatCooldown = 5;
    }

    public void killedPlayer(Player victim) {
        incrementKillStreak();
        incrementTotalKills();

        ExtendedPlayer extendedTarget = from(victim);
        int xpReward = 10 + (int) (extendedTarget.getLevel() * 0.25);
        if (KitPvp.INSTANCE.getCustomEventHandler().getActiveEvent() == CustomEventHandler.DOUBLE_EXPERIENCE_EVENT)
            xpReward *= 2;

        addExperiencePoints(xpReward);

        if (gameState == GameState.IN_GAME)
            getPlayer().getInventory().addItem(getSelectedKit().getKillRewards());
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

package io.github.chaosdave34.kitpvp;

import io.github.chaosdave34.ghutils.GHUtils;
import io.github.chaosdave34.ghutils.utils.MathUtils;
import io.github.chaosdave34.kitpvp.challenges.Challenge;
import io.github.chaosdave34.kitpvp.companions.Companion;
import io.github.chaosdave34.kitpvp.customevents.CustomEventHandler;
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent;
import io.github.chaosdave34.kitpvp.kits.ElytraKitHandler;
import io.github.chaosdave34.kitpvp.kits.Kit;
import io.github.chaosdave34.kitpvp.kits.KitHandler;
import io.github.chaosdave34.kitpvp.textdisplays.TextDisplays;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
    private String selectedElytraKitId;
    private GameType currentGame;

    private int experiencePoints;
    private int coins;
    private int bounty;

    private transient GameState gameState;
    private transient Scoreboard scoreboard;
    private transient int combatCooldown;

    private transient Entity morph;
    private transient Mob companion;

    private transient int killStreak;

    private Map<GameType, Integer> highestKillStreaks;
    private Map<GameType, Integer> totalKills;
    private Map<GameType, Integer> totalDeaths;

    private String projectileTrailId;
    private String killEffectId;

    private List<String> dailyChallenges;

    public ExtendedPlayer(Player p) {
        uuid = p.getUniqueId();
        gameState = GameState.SPAWN;
        currentGame = GameType.KITS;
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
        return gameState == GameState.SPAWN || gameState == GameState.ELYTRA_SPAWN;
    }

    public boolean inGame() {
        return gameState == GameState.IN_GAME || gameState == GameState.ELYTRA_IN_GAME;
    }

    public void setSelectedKitId(String id) {
        selectedKitId = id;
        updateScoreboardLines();
    }

    public void setSelectedElytraKitId(String id) {
        selectedElytraKitId = id;
        updateScoreboardLines();
    }

    public Kit getSelectedKit() {
        return selectedKitId == null ? null : KitPvp.INSTANCE.getKitHandler().getKits().get(selectedKitId);
    }

    public Kit getSelectedElytraKit() {
        return selectedElytraKitId == null ? null : KitPvp.INSTANCE.getElytraKitHandler().getKits().get(selectedElytraKitId);
    }

    public void spawn() {
        spawn(getCurrentGame());
    }

    public void spawn(GameType gameType) {
        Player p = getPlayer();
        if (p == null) return;

        p.setGameMode(GameMode.SURVIVAL);

        currentGame = gameType;
        if (currentGame == null) currentGame = GameType.KITS;

        if (currentGame == GameType.KITS) {
            p.teleport(new Location(Bukkit.getWorld("world"), 2.0, 120.0, 10.0, 180, 0));
            gameState = GameState.SPAWN;
        } else if (currentGame == GameType.ELYTRA) {
            p.teleport(new Location(Bukkit.getWorld("world_elytra"), 16.0, 200, 0.0, 90, 0));
            gameState = GameState.ELYTRA_SPAWN;
        }

        unmorph();
        removeCompanion();

        p.setFoodLevel(20);
        p.setSaturation(5);
        p.setExhaustion(0);

        p.setFireTicks(0);
        p.setFreezeTicks(0);

        p.setItemOnCursor(null);
        p.clearActiveItem();
        p.closeInventory(InventoryCloseEvent.Reason.DEATH);

        killStreak = 0;
        combatCooldown = 0;

        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("default", Criteria.DUMMY, Component.text("KitPvP", NamedTextColor.YELLOW, TextDecoration.BOLD));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            Objective hearts = scoreboard.registerNewObjective("hearts", Criteria.HEALTH, Component.text("health", NamedTextColor.RED));
            hearts.setDisplaySlot(DisplaySlot.BELOW_NAME);

            p.setScoreboard(scoreboard);
        }

        if (getSelectedKit() == null) setSelectedKitId(KitHandler.CLASSIC.getId());
        if (getSelectedElytraKit() == null) setSelectedElytraKitId(ElytraKitHandler.KNIGHT.getId());

        if (currentGame == GameType.KITS)
            getSelectedKit().apply(p);
        else if (currentGame == GameType.ELYTRA)
            getSelectedElytraKit().apply(p);

        new PlayerSpawnEvent(p).callEvent();

        updateScoreboardLines();
        updateDisplayName();

        p.getWorld().getEntitiesByClass(Trident.class).forEach(trident -> {
            if (trident.getShooter() instanceof Player shooter)
                if (p.getEntityId() == shooter.getEntityId())
                    trident.remove();
        });
    }

    public void updateDisplayName() {
        Player p = getPlayer();

        String icon = "";
        if (getCurrentGame() == GameType.KITS) icon = "⚔";
        else if (getCurrentGame() == GameType.ELYTRA) icon = "\uD83D\uDEE1";

        Component name = Component.text(icon)
                .append(Component.text(" ["))
                .append(Component.text(getLevel()))
                .append(Component.text("] "))
                .append(p.name());

        if (!p.displayName().equals(name)) {
            p.displayName(name);
        }

        if (bounty > 0) {
            name = name.append(Component.space().append(Component.text(bounty, NamedTextColor.GOLD)));
        }
        p.playerListName(name);
    }

    public void updateScoreboardLines() {
        Player p = getPlayer();
        if (p == null) return;

        Objective objective = scoreboard.getObjective("default");
        if (objective == null) return;

        scoreboard.getEntries().forEach((entry) -> scoreboard.resetScores(entry));

        objective.getScore("Level: " + getLevel()).setScore(7);
        objective.getScore("Missing XP: §b" + getMissingExperience()).setScore(6);
        objective.getScore("Coins: §6" + getCoins()).setScore(5);
        objective.getScore("  ").setScore(4);
        String kitName = "None";
        if (currentGame == GameType.KITS)
            kitName = getSelectedKit() == null ? "None" : getSelectedKit().getName();

        else if (currentGame == GameType.ELYTRA)
            kitName = getSelectedElytraKitId() == null ? "None" : getSelectedElytraKit().getName();

        objective.getScore("Kit: " + kitName).setScore(3);
        objective.getScore("Kill Streak: " + killStreak).setScore(2);
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
        killStreak++;

        if (killStreak % 5 == 0) {
            Bukkit.broadcast((Component.text(getPlayer().getName() + " has reached a kill streak of " + killStreak)));
            receivedBounty(killStreak / 5 * 100);
        }

        if (killStreak > highestKillStreaks.get(currentGame)) {
            highestKillStreaks.put(currentGame, killStreak);
            updatePersonalStatisticsDisplay(currentGame);
        }

        checkHighestKillStreakHighscore(currentGame);
        updateScoreboardLines();
    }

    private void updatePersonalStatisticsDisplay(GameType gameType) {
        switch (gameType) {
            case KITS ->
                    GHUtils.getTextDisplayHandler().updateTextDisplay(getPlayer(), TextDisplays.PERSONAL_STATISTICS_KITS);
            case ELYTRA ->
                    GHUtils.getTextDisplayHandler().updateTextDisplay(getPlayer(), TextDisplays.PERSONAL_STATISTICS_ELYTRA);
        }
    }

    public int getHighestKillStreak(GameType gameType) {
        if (highestKillStreaks == null) highestKillStreaks = new HashMap<>();

        if (!highestKillStreaks.containsKey(gameType)) highestKillStreaks.put(gameType, 0);

        return highestKillStreaks.get(gameType);
    }

    private void checkHighestKillStreakHighscore(GameType gameType) {
        Map<UUID, Integer> highestKillStreaks = KitPvp.INSTANCE.getHighestKillStreaks(gameType);
        if ((highestKillStreaks.size() < 5 && !highestKillStreaks.containsKey(uuid)) || getKillStreak() > Collections.min(highestKillStreaks.values())) {

            if (highestKillStreaks.size() == 5 && !highestKillStreaks.containsKey(uuid)) {
                for (UUID key : highestKillStreaks.keySet()) {
                    if (Objects.equals(highestKillStreaks.get(key), Collections.min(highestKillStreaks.values()))) {
                        highestKillStreaks.remove(key);
                        break;
                    }
                }
            }

            highestKillStreaks.put(uuid, getKillStreak());

            if (gameType == GameType.KITS) {
                GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_KILL_STREAKS_KITS);
            } else if (gameType == GameType.ELYTRA) {
                GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_KILL_STREAKS_ELYTRA);
            }
        }
    }

    public int getTotalKills(GameType gameType) {
        if (totalKills == null) totalKills = new HashMap<>();

        if (!totalKills.containsKey(gameType)) totalKills.put(gameType, 0);

        return totalKills.get(gameType);
    }

    public void incrementTotalKills() {
        totalKills.put(currentGame, totalKills.get(currentGame) + 1);
        updatePersonalStatisticsDisplay(currentGame);
    }

    public int getTotalDeaths(GameType gameType) {
        if (totalDeaths == null) totalDeaths = new HashMap<>();


        if (!totalDeaths.containsKey(gameType)) totalDeaths.put(gameType, 0);

        return totalDeaths.get(gameType);
    }

    public void incrementTotalDeaths() {
        totalDeaths.put(currentGame, totalDeaths.get(currentGame) + 1);
        updatePersonalStatisticsDisplay(currentGame);
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

                GHUtils.getTextDisplayHandler().updateTextDisplayForAll(TextDisplays.HIGHEST_LEVELS_KITS);
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

    public void addCoins(int amount) {
        coins += amount;
        updateScoreboardLines();
    }

    public boolean purchase(int amount) {
        if (amount <= coins) {
            coins -= amount;
            updateScoreboardLines();
            return true;
        }
        return false;
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

    public void reviveCompanion() {
        if (companion != null) {
            removeCompanion();
            spawnCompanion();
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

    public void receivedBounty(int amount) {
        Component message;
        if (bounty == 0) {
            message = Component.text("A bounty of " + amount + " coins has been placed on " + getPlayer().getName() + ".");
        } else {
            message = Component.text("The bounty on " + getPlayer().getName() + " has been increased by " + amount + " coins to a total of " + (bounty + amount) + " coins.");
        }

        Bukkit.broadcast(message);
        bounty += amount;

        updateDisplayName();
    }

    public void killedPlayer(Player victim) {
        incrementKillStreak();
        incrementTotalKills();

        ExtendedPlayer extendedVictim = from(victim);
        int xpReward = 10 + (int) (extendedVictim.getLevel() * 0.25);
        if (KitPvp.INSTANCE.getCustomEventHandler().getActiveEvent() == CustomEventHandler.DOUBLE_COINS_AND_EXPERIENCE_EVENT) {
            xpReward *= 2;
        }

        int coinReward = (int) (xpReward * 1.5);

        if (extendedVictim.getBounty() > 0) {
            coinReward += extendedVictim.getBounty();
            extendedVictim.claimBounty(getPlayer());
        }

        addExperiencePoints(xpReward);
        addCoins(coinReward);

        Player p = getPlayer();
        Component info = Component.text("Killed " + victim.getName() + ": ")
                .append(Component.text("+" + xpReward + "XP", NamedTextColor.AQUA))
                .append(Component.space())
                .append(Component.text("+" + coinReward + " coins", NamedTextColor.GOLD));
        p.sendActionBar(info);

        reviveCompanion();

        if (gameState == GameState.IN_GAME) {
            p.getInventory().addItem(getSelectedKit().getKillRewards());
        } else if (gameState == GameState.ELYTRA_IN_GAME) {
            p.getInventory().addItem(getSelectedElytraKit().getKillRewards());
        }

        KitPvp.INSTANCE.getCosmeticHandler().triggerKillEffect(getPlayer(), p);
    }

    public void claimBounty(Player claimer) {
        Bukkit.broadcast(Component.text("The bounty on " + getPlayer().getName() + " has been claimed by " + claimer.getName() + "."));

        bounty = 0;

        updateDisplayName();
    }

    public enum GameType {
        KITS,
        ELYTRA
    }

    public enum GameState {
        SPAWN("§aIdle"),
        ELYTRA_SPAWN("§aIdle"),
        IN_GAME("§eActive"),
        ELYTRA_IN_GAME("§eActive"),
        DEBUG("§0Debug");

        private final String displayName;

        GameState(String name) {
            this.displayName = name;
        }
    }
}

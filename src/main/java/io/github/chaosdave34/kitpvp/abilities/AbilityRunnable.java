package io.github.chaosdave34.kitpvp.abilities;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public abstract class AbilityRunnable extends BukkitRunnable {
    private final ExtendedPlayer extendedPlayer;
    private final ExtendedPlayer.GameType gameType;
    private final int deathCount;

    protected AbilityRunnable(ExtendedPlayer extendedPlayer) {
        this.extendedPlayer = extendedPlayer;
        this.gameType = extendedPlayer.getCurrentGame();
        this.deathCount = extendedPlayer.getTotalDeaths(gameType);
    }

    protected AbilityRunnable(Player p) {
        this(ExtendedPlayer.from(p));
    }

    @Override
    public void run() {
        if (!extendedPlayer.inGame()) cancel();
        if (gameType != extendedPlayer.getCurrentGame()) cancel();
        if (deathCount != extendedPlayer.getTotalDeaths(gameType)) cancel();
        runInGame();
    }

    public abstract void runInGame();

    public static BukkitTask runTaskLater(@NotNull Plugin plugin, @NotNull Runnable task, ExtendedPlayer extendedPlayer, long delay) {
        return new AbilityRunnable(extendedPlayer) {
            @Override
            public void runInGame() {
                task.run();
            }
        }.runTaskLater(plugin, delay);
    }

    public static BukkitTask runTaskLater(@NotNull Plugin plugin, @NotNull Runnable task, Player player, long delay) {
        return runTaskLater(plugin, task, ExtendedPlayer.from(player), delay);
    }
}

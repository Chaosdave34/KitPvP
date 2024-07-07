package io.github.chaosdave34.kitpvp.customevents;

import io.github.chaosdave34.kitpvp.KitPvp;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CustomEvent implements Listener {
    private final String name;
    private final int duration;
    protected boolean cancelled;
    private BossBar bossBar;

    public CustomEvent(String name, int duration) {
        this.name = name;
        this.duration = duration;
        this.cancelled = false;
    }

    public void trigger() {
        World world = Bukkit.getWorld("world");
        if (world == null) return;

        int minutes = duration / 60;
        int seconds = duration % 60;
        String time = String.format("%02d:%02d", minutes, seconds);

        Bukkit.broadcast(Component.text("A new event has started: " + name + " for " + time));

        bossBar = BossBar.bossBar(Component.text(name + " " + time), 1, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
        Bukkit.getServer().showBossBar(bossBar);

        start();

        new BukkitRunnable() {
            int duration_left = duration;

            @Override
            public void run() {
                if (duration_left == 0 || cancelled) {
                    this.cancel();
                    Bukkit.getServer().hideBossBar(bossBar);
                    stop();
//                    KitPvp.INSTANCE.getCustomEventHandler().stopActiveEvent();
                }

                int minutes = duration_left / 60;
                int seconds = duration_left % 60;
                String timeLeft;
                timeLeft = String.format("%02d:%02d", minutes, seconds);

                bossBar.name(Component.text(name + " | Ends in " + timeLeft));
                bossBar.progress((float) duration_left / duration);

                duration_left--;
            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, 20);
    }

    public boolean isActive() {
//        return this == KitPvp.INSTANCE.getCustomEventHandler().getActiveEvent();
        return false;
    }

    public void start() {
    }


    public void stop() {
    }

    public String getName() {
        return this.name;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

}

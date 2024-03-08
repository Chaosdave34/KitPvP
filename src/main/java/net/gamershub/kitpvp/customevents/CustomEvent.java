package net.gamershub.kitpvp.customevents;

import lombok.Getter;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public abstract class CustomEvent {
    private final String name;
    private final int duration;
    private BossBar bossBar;

    public CustomEvent(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public void trigger() {
        World world = Bukkit.getWorld("world");
        if (world == null) return;

        int minutes = duration / 60;
        int seconds = duration % 60;
        String time = String.format("%02d:%02d", minutes, seconds);

        Bukkit.broadcast(Component.text("A new event has started: " + name + " for " + time));

        bossBar = BossBar.bossBar(Component.text(name + " " + time), 1, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
        world.showBossBar(bossBar);

        new BukkitRunnable() {
            int duration_left = duration;

            @Override
            public void run() {
                if (duration_left == 0) {
                    this.cancel();
                    world.hideBossBar(bossBar);
                    stop();
                    KitPvpPlugin.INSTANCE.getCustomEventHandler().stopActiveEvent();
                }

                int minutes = duration_left / 60;
                int seconds = duration_left % 60;
                String timeLeft;
                timeLeft = String.format("%02d:%02d", minutes, seconds);

                bossBar.name(Component.text(name + " | Ends in " + timeLeft));
                bossBar.progress((float) duration_left / duration);

                duration_left--;
            }
        }.runTaskTimer(KitPvpPlugin.INSTANCE, 0, 20);
    }


    public void stop() {
    }
}

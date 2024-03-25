package io.github.chaosdave34.kitpvp.customevents;

import lombok.Getter;
import io.github.chaosdave34.ghlib.Utils;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.customevents.impl.SimpleEvent;
import io.github.chaosdave34.kitpvp.customevents.impl.SupplyDropEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

@Getter
public class CustomEventHandler implements Listener {
    private final ArrayList<CustomEvent> customEvents = new ArrayList<>();
    private CustomEvent activeEvent;
    private boolean enabled;

    public static CustomEvent DOUBLE_EXPERIENCE_EVENT;
    public static CustomEvent HALVED_COOLDOWN_EVENT;
    public static CustomEvent SUPPLY_DROP_EVENT;

    public CustomEventHandler() {
        enabled = false;

        DOUBLE_EXPERIENCE_EVENT = registerCustomEvent(new SimpleEvent("2x Experience", 5 * 60));
        HALVED_COOLDOWN_EVENT = registerCustomEvent(new SimpleEvent("Halved Ability Cooldown", 5 * 60));
        SUPPLY_DROP_EVENT = registerCustomEvent(new SupplyDropEvent());
    }

    private CustomEvent registerCustomEvent(CustomEvent customEvent) {
        Utils.registerEvents(customEvent);
        customEvents.add(customEvent);
        return customEvent;
    }

    private void start() {
        enabled = true;
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            Random random = new Random();
            CustomEvent event = customEvents.get(random.nextInt(customEvents.size()));
            event.trigger();
            activeEvent = event;
        } else {
            enabled = false;
        }
    }

    public void stopActiveEvent() {
        activeEvent = null;
        new BukkitRunnable() {
            @Override
            public void run() {
                start();
            }
        }.runTaskLater(KitPvp.INSTANCE, 10 * 60 * 20);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!enabled) start();
    }
}


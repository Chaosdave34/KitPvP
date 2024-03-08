package net.gamershub.kitpvp.customevents;

import lombok.Getter;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.customevents.impl.SimpleEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

@Getter
public class CustomEventHandler {
    private final ArrayList<CustomEvent> customEvents = new ArrayList<>();
    private CustomEvent activeEvent;

    public static CustomEvent DOUBLE_EXPERIENCE_EVENT;
    public static CustomEvent HALVED_COOLDOWN_EVENT;

    public CustomEventHandler() {
        DOUBLE_EXPERIENCE_EVENT = registerCustomEvent(new SimpleEvent("2x Experience", 5 * 60));
        HALVED_COOLDOWN_EVENT = registerCustomEvent(new SimpleEvent("Halved Ability Cooldown", 5 * 60));

        new BukkitRunnable() {
            @Override
            public void run() {
                start();
            }
        }.runTaskLater(KitPvpPlugin.INSTANCE, 20 * 20);
    }

    private CustomEvent registerCustomEvent(CustomEvent customEvent) {
        customEvents.add(customEvent);
        return customEvent;
    }

    private void start() {
        Random random = new Random();
        CustomEvent event = customEvents.get(random.nextInt(customEvents.size()));
        event.trigger();
        activeEvent = event;
    }

    public void stopActiveEvent() {
        activeEvent = null;
        new BukkitRunnable() {
            @Override
            public void run() {
                start();
            }
        }.runTaskLater(KitPvpPlugin.INSTANCE, 10 * 60 * 20);
    }
}


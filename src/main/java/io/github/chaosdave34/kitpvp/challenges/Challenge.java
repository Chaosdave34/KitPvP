package io.github.chaosdave34.kitpvp.challenges;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public abstract class Challenge implements Listener {
    protected String id;
    protected String name;
    protected int amount;
    protected final Map<Player, Integer> progress = new HashMap<>();

    public Challenge(String id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    protected void incrementProgress(Player p) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);

        if (getProgress(p) == amount - 1) {
            int xpReward = 50;
//            if (KitPvp.INSTANCE.getCustomEventHandler().getActiveEvent() == CustomEventHandler.DOUBLE_COINS_AND_EXPERIENCE_EVENT)
//                xpReward *= 2;

            p.sendMessage(Component.text("You have completed a daily challenge! +" + xpReward + "XP"));
            extendedPlayer.addExperiencePoints(xpReward);
        }

        if (getProgress(p) < amount) {
            if (progress.containsKey(p))
                progress.put(p, progress.get(p) + 1);
            else
                progress.put(p, 1);
        }


        extendedPlayer.updatePlayerListFooter();
    }

    public int getProgress(Player p) {
        if (!progress.containsKey(p))
            progress.put(p, 0);

        return progress.get(p);
    }

    public void resetProgress(Player p) {
        progress.remove(p);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAmount() {
        return this.amount;
    }

    public Map<Player, Integer> getProgress() {
        return this.progress;
    }
}

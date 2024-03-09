package net.gamershub.kitpvp.challenges.impl;

import net.gamershub.kitpvp.challenges.Challenge;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TwentyFiveKillsChallenge extends Challenge {
    public TwentyFiveKillsChallenge() {
        super("twenty_five_kills", "25 Kills", 25);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent) {
            if (damageEvent.getDamager() instanceof Player p) {
                incrementProgress(p);
            }
        }
    }
}

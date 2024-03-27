package io.github.chaosdave34.kitpvp.challenges.impl;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.challenges.Challenge;
import io.github.chaosdave34.kitpvp.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

// todo: elytra kits and randomize
public class TenKillsWithKit extends Challenge {
    private final Kit kit;

    public TenKillsWithKit(Kit kit) {
        super("ten_kills_with_" + kit.getId() + "_kit", "10 Kills with " + kit.getName() + " Kit", 10);
        this.kit = kit;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent) {
            if (damageEvent.getDamager() instanceof Player p) {
                if (ExtendedPlayer.from(p).getSelectedKit() == kit)
                    incrementProgress(p);
            }
        }
    }
}

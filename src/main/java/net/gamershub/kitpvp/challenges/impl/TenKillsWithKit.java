package net.gamershub.kitpvp.challenges.impl;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.challenges.Challenge;
import net.gamershub.kitpvp.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TenKillsWithKit extends Challenge {
    private Kit kit;

    public TenKillsWithKit(Kit kit) {
        super("ten_kills_with_" + kit.getId() + "_kit", "10 Kills with " + kit.getName() + " Kit", 10);
        this.kit = kit;
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent) {
            if (damageEvent.getDamager() instanceof Player p) {
                if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getSelectedKit() == kit)
                    incrementProgress(p);
            }
        }
    }
}

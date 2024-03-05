package net.gamershub.kitpvp.listener;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GamePlayerDeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.IN_GAME) {

            extendedPlayer.incrementTotalDeaths();

            Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, extendedPlayer::spawnPlayer, 1);

            e.setCancelled(true);

            // Death Messages
            String name = p.getName();
            String message = PlainTextComponentSerializer.plainText().serialize(e.deathMessage());

            EntityDamageEvent lastDamageEvent = p.getLastDamageCause();
            if (lastDamageEvent != null) {
                // Damage by Entity
                if (lastDamageEvent instanceof EntityDamageByEntityEvent lastDamageByEntityEvent) {
                    Entity damager = lastDamageByEntityEvent.getDamager();
                    // Lightning
                    if (damager instanceof LightningStrike lightningStrike) {
                        if (lightningStrike.getCausingPlayer() != null) {
                            message = name + " was killed by " + lightningStrike.getCausingPlayer().getName() + " with lightning";
                            KitPvpPlugin.INSTANCE.getExtendedPlayer(lightningStrike.getCausingPlayer()).killedPlayer(p);
                        }
                    }
                }
            }

            Bukkit.broadcast(Component.text("☠ ", NamedTextColor.RED)
                    .append(Component.text(message, NamedTextColor.GRAY)));
        }
    }
}

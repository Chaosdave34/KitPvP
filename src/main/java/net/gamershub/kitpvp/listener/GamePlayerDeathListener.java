package net.gamershub.kitpvp.listener;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;

public class GamePlayerDeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);

        if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.IN_GAME) {

            Location spawn = new Location(Bukkit.getWorld("world"), 0.5, 100.5, -8.5, 0, 0);
            Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, () -> p.teleport(spawn), 1);

            extendedPlayer.setGameState(ExtendedPlayer.GameState.SPAWN);

            p.setFireTicks(0);

            p.setHealth(e.getReviveHealth());

            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }

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
                        } else {
                            message = name + " was struck by lightning";
                        }
                    }
                }
            }

            Bukkit.broadcast(Component.text("☠ ", NamedTextColor.RED)
                    .append(Component.text(message, NamedTextColor.GRAY)));
        }
    }
}

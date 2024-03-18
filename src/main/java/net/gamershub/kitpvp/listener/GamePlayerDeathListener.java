package net.gamershub.kitpvp.listener;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.persistentdatatypes.UUIDPersistentDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.UUID;

public class GamePlayerDeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        e.setKeepInventory(true);

        if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.IN_GAME) {

            extendedPlayer.incrementTotalDeaths();

            Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, extendedPlayer::spawnPlayer, 1);

            e.setCancelled(true);

            // Death Messages
            String name = p.getName();
            Component deathMessage = e.deathMessage();
            String message = deathMessage == null ? "" : PlainTextComponentSerializer.plainText().serialize(deathMessage);

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
                    // Turret
                    if (damager instanceof Arrow arrow) {
                        if (arrow.getShooter() instanceof Husk husk) {

                            PersistentDataContainer container = husk.getPersistentDataContainer();
                            NamespacedKey ownerKey = new NamespacedKey(KitPvpPlugin.INSTANCE, "owner");
                            if (container.has(ownerKey)) {

                                UUID turretOwnerUUUID = container.get(ownerKey, new UUIDPersistentDataType());
                                if (turretOwnerUUUID != null) {
                                    message = name + " was killed by " + Bukkit.getOfflinePlayer(turretOwnerUUUID).getName() + "'s turret";
                                }
                            }
                        }
                    }
                }
            }

            Bukkit.broadcast(Component.text("☠ ", NamedTextColor.RED)
                    .append(Component.text(message, NamedTextColor.GRAY)));
        }
    }
}

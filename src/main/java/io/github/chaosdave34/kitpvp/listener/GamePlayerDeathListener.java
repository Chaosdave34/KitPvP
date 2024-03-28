package io.github.chaosdave34.kitpvp.listener;

import io.github.chaosdave34.ghutils.utils.PDCUtils;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.damagetype.DamageTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class GamePlayerDeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        e.setKeepInventory(true);

        if (extendedPlayer.inGame()) {

            extendedPlayer.incrementTotalDeaths();

            Bukkit.getScheduler().runTaskLater(KitPvp.INSTANCE, () -> extendedPlayer.spawn(), 1);

            e.setCancelled(true);

            // Death Messages
            String name = p.getName();
            Component deathMessage = e.deathMessage();
            String message = deathMessage == null ? "" : PlainTextComponentSerializer.plainText().serialize(deathMessage);

            EntityDamageEvent lastDamageEvent = p.getLastDamageCause();
            if (lastDamageEvent != null) {
                DamageSource damageSource = lastDamageEvent.getDamageSource();
                DamageType damageType = damageSource.getDamageType();

                // Damage by Entity
                if (lastDamageEvent instanceof EntityDamageByEntityEvent lastDamageByEntityEvent) {
                    Entity damager = lastDamageByEntityEvent.getDamager();

                    // Kill Effect
                    if (damager instanceof Player killer) {
                        ExtendedPlayer.from(killer).killedPlayer(p);
                    } else if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player killer) {
                        ExtendedPlayer.from(killer).killedPlayer(p);
                    }

                    // Lightning
                    else if (damager instanceof LightningStrike lightningStrike) {
                        if (lightningStrike.getCausingPlayer() != null) {
                            Player killer = lightningStrike.getCausingPlayer();
                            message = name + " was killed by " + killer.getName() + " with lightning";

                            ExtendedPlayer.from(killer).killedPlayer(p);
                            KitPvp.INSTANCE.getCosmeticHandler().triggerKillEffect(killer, p);
                        }
                    }
                    // Turret
                    if (damager instanceof Firework firework) {
                        if (firework.getShooter() instanceof Husk husk) {
                            UUID turretOwnerUUUID = PDCUtils.getOwner(husk);
                            if (turretOwnerUUUID != null) {
                                Player turretOwner = Bukkit.getPlayer(turretOwnerUUUID);
                                if (turretOwner != null) {
                                    message = name + " was killed by " + turretOwner.getName() + "'s turret";

                                    ExtendedPlayer.from(turretOwner).killedPlayer(p);
                                    KitPvp.INSTANCE.getCosmeticHandler().triggerKillEffect(turretOwner, p);
                                }
                            }
                        }
                    }
                    // Companion
                    else if (damager.hasMetadata("companion")) {
                        UUID ownerUUID = PDCUtils.getOwner(damager);
                        if (ownerUUID != null) {
                            Player companionOwner = Bukkit.getPlayer(ownerUUID);
                            if (companionOwner != null) {
                                message = name + " was killed by " + companionOwner.getName() + "'s companion";

                                ExtendedPlayer.from(companionOwner).killedPlayer(p);
                                KitPvp.INSTANCE.getCosmeticHandler().triggerKillEffect(companionOwner, p);
                            }
                        }

                    }
                } else if (damageType == DamageTypes.LAND) {
                    message = name + " tried to land";
                } else if (damageType == DamageTypes.ESCAPE) {
                    message = name + " tried to escape";
                }

                Bukkit.broadcast(Component.text("☠ ", NamedTextColor.RED)
                        .append(Component.text(message, NamedTextColor.GRAY)));
            }
        }
    }
}
package io.github.chaosdave34.kitpvp.companions;

import io.github.chaosdave34.ghutils.utils.PDCUtils;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.companions.impl.AllayCompanion;
import io.github.chaosdave34.kitpvp.companions.impl.ZombifiedPiglinCompanion;
import io.github.chaosdave34.kitpvp.events.EntityReceiveDamageByEntityEvent;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Pose;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CompanionHandler implements Listener {
    public static Companion ALLAY;
    public static Companion ZOMBIFIED_PIGLIN;

    public CompanionHandler() {
        ALLAY = registerCompanion(new AllayCompanion());
        ZOMBIFIED_PIGLIN = registerCompanion(new ZombifiedPiglinCompanion());
    }

    private Companion registerCompanion(Companion companion) {
        return companion;
    }

    @EventHandler
    public void onCompanionDeath(EntityDeathEvent e) {
        org.bukkit.entity.@NotNull LivingEntity entity = e.getEntity();
        if (entity.hasMetadata("companion")) {
            e.setCancelled(true);

            entity.getWorld().playSound(Sound.sound(org.bukkit.Sound.ENTITY_PIGLIN_DEATH, Sound.Source.HOSTILE, 1, 1));

            entity.setAI(false);
            entity.setSilent(true);
            entity.setPose(Pose.SLEEPING);
            entity.setInvulnerable(true);
        }
    }

    @EventHandler
    public void onCompanionDamage(EntityReceiveDamageByEntityEvent e) {
        if (e.getEntity().hasMetadata("companion")) {

            UUID ownerUUID = PDCUtils.getOwner(e.getEntity());

            if (ownerUUID != null && ExtendedPlayer.from(ownerUUID).inSpawn())
                e.setCancelled(true);

            if (e.getDamager().getUniqueId().equals(ownerUUID))
                e.setCancelled(true);
        }
    }
}

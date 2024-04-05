package io.github.chaosdave34.kitpvp.abilities.impl.devil;

import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable;
import io.github.chaosdave34.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FireStormAbility extends Ability {
    public FireStormAbility() {
        super("fire_storm", "Fire Storm", AbilityType.RIGHT_CLICK, 20);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Cause a storm of fire damaging",
                "nearby enemies."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        new AbilityRunnable(p) {
            int counter = 0;

            @Override
            public void runInGame() {
                counter += 10;

                if (counter == 10 * 20) cancel();

                for (Entity entity : p.getNearbyEntities(3, 3, 3)) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.damage(2, DamageSource.builder(DamageType.ON_FIRE).withDirectEntity(p).withCausingEntity(p).build());
                        livingEntity.setFireTicks(15);
                    }
                }

                Location location = p.getLocation();
                for (int positionCounter = 0; positionCounter < 6; positionCounter++) {
                    Location position = location.clone();

                    position.add(3 * Math.cos(positionCounter), 0, 3 * Math.sin(positionCounter));

                    for (int yCounter = 0; yCounter < 25; yCounter++) {
                        position.add(0, yCounter * 0.2, 0);
                        position.getWorld().spawnParticle(Particle.FLAME, position, 1, 0, 0, 0, 0);
                    }
                }
            }
        }.runTaskTimer(KitPvp.INSTANCE, 0, 10);

        return true;
    }
}

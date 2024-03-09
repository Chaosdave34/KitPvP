package net.gamershub.kitpvp.abilities.impl.magician;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MagicAttackAbility extends Ability {
    public MagicAttackAbility() {
        super("magic_attack", "Magic Attack", AbilityType.LEFT_CLICK, 1);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("MAGIC!");
    }

    @Override
    public boolean onAbility(Player p) {
        Location location = p.getEyeLocation().subtract(0, 0.25, 0);

        for (int i = 0; i < 30; i++) {
            p.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(location.getDirection().clone().multiply(i * 0.2)), 1, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1));
        }

        Entity target = p.getTargetEntity(8);
        if (target instanceof LivingEntity livingEntity) {
            livingEntity.damage(4, p);
        }
        return true;
    }
}

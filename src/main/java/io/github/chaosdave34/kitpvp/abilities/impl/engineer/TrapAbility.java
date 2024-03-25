package io.github.chaosdave34.kitpvp.abilities.impl.engineer;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrapAbility extends Ability {
    public TrapAbility() {
        super("trap", "Trap", AbilityType.LEFT_CLICK, 10);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Traps the player you are",
                "looking at in a 10 block radius,"
        );
    }

    @Override
    public boolean onAbility(Player p) {
        Entity target = p.getTargetEntity(10);
        if (target instanceof LivingEntity livingEntity) {

            if (target instanceof Player player) {
                if (ExtendedPlayer.from(player).inSpawn())
                    return false;
            }

            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 255));
            return true;
        }
        return false;
    }
}

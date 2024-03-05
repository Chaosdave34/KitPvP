package net.gamershub.kitpvp.abilities.impl.assassin;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.gamershub.kitpvp.events.PlayerSpawnEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HauntAbility extends Ability {
    public HauntAbility() {
        super("haunt", "Haunt", AbilityType.RIGHT_CLICK, 60);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Gives you a speed boost and makes",
                "you invisible for 5 seconds.",
                "Runs out when you hit a player."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 10));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 1));
        p.addScoreboardTag("haunt_ability");
        return true;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player damager) {
            if (damager.getScoreboardTags().contains("haunt_ability")) {
                damager.removePotionEffect(PotionEffectType.SPEED);
                damager.removePotionEffect(PotionEffectType.INVISIBILITY);
                damager.removeScoreboardTag("haunt_ability");
            }
        }
    }

    @EventHandler
    public void onSpawn(PlayerSpawnEvent e) {
        Player p = e.getPlayer();
        if (p.getScoreboardTags().contains("haunt_ability")) {
            p.removePotionEffect(PotionEffectType.SPEED);
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
            p.removeScoreboardTag("haunt_ability");
        }
    }
}

package io.github.chaosdave34.kitpvp.abilities.impl.assassin;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable;
import io.github.chaosdave34.kitpvp.events.EntityDealDamageEvent;
import io.github.chaosdave34.kitpvp.kits.Kit;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HauntAbility extends Ability {
    public HauntAbility() {
        super("haunt", "Haunt", Type.RIGHT_CLICK, 60);
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
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 10, 9));
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5 * 10, 0));
        p.addScoreboardTag("haunt_ability");

        p.getInventory().setHelmet(new ItemStack(Material.AIR));
        p.getInventory().setChestplate(new ItemStack(Material.AIR));
        p.getInventory().setLeggings(new ItemStack(Material.AIR));
        p.getInventory().setBoots(new ItemStack(Material.AIR));

        AbilityRunnable.runTaskLater(KitPvp.INSTANCE, () -> removeEffects(p), p, 5 * 20);
        return true;
    }

    @EventHandler
    public void onPlayerAttack(EntityDealDamageEvent e) {
        if (e.getDamager() instanceof Player damager) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.from(damager);
            if (extendedPlayer.getGameState() == ExtendedPlayer.GameState.KITS_IN_GAME && damager.getScoreboardTags().contains("haunt_ability")) {
                removeEffects(damager);
            }
        }
    }

    private void removeEffects(Player p) {
        p.removePotionEffect(PotionEffectType.SPEED);
        p.removePotionEffect(PotionEffectType.INVISIBILITY);

        p.removeScoreboardTag("haunt_ability");

        Kit kit = ExtendedPlayer.from(p).getSelectedKit();
        p.getInventory().setHelmet(kit.getHeadContent());
        p.getInventory().setChestplate(kit.getChestContent());
        p.getInventory().setLeggings(kit.getLegsContent());
        p.getInventory().setBoots(kit.getFeetContent());
    }
}

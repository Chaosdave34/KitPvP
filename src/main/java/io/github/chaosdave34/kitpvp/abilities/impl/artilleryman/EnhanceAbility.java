package io.github.chaosdave34.kitpvp.abilities.impl.artilleryman;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnhanceAbility extends Ability {
    public EnhanceAbility() {
        super("enhance", "Enhance", AbilityType.LEFT_CLICK, 60);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Instantly load your rocket launcher",
                "for 20 seconds."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        ItemStack crossbow = p.getInventory().getItemInMainHand();
        crossbow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 5);
        new BukkitRunnable() {
            final int checkDeath = extendedPlayer.getTotalDeaths(ExtendedPlayer.GameType.KITS);
            @Override
            public void run() {
                if (checkDeath == extendedPlayer.getTotalDeaths(ExtendedPlayer.GameType.KITS)) {
                    crossbow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 3);
                }
            }
        }.runTaskLater(KitPvp.INSTANCE, 20 * 5);
        return true;
    }
}

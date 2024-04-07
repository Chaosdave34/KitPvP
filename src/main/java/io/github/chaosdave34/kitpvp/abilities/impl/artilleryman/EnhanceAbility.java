package io.github.chaosdave34.kitpvp.abilities.impl.artilleryman;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable;

import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnhanceAbility extends Ability {
    public EnhanceAbility() {
        super("enhance", "Enhance", Type.LEFT_CLICK, 60);
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
        new AbilityRunnable(extendedPlayer) {
            @Override
            public void runInGame() {
                crossbow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 3);
            }
        }.runTaskLater(KitPvp.INSTANCE, 20 * 5);
        return true;
    }
}

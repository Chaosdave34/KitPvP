package io.github.chaosdave34.kitpvp.abilities.impl.provoker;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpookAbility extends Ability {
    public SpookAbility() {
        super("spook", "Spook", Type.RIGHT_CLICK, 30);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Places a pumpkin on the",
                "player you are looking at",
                "in a 20 block radius for 5s."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        Entity target = p.getTargetEntity(10);
        if (target instanceof Player player) {

            if (ExtendedPlayer.from(player).inSpawn())
                return false;

            ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
            pumpkin.addEnchantment(Enchantment.BINDING_CURSE, 1);
            player.getInventory().setHelmet(pumpkin);

            ExtendedPlayer extendedTargetPlayer = ExtendedPlayer.from(p);

            AbilityRunnable.runTaskLater(KitPvp.INSTANCE, () -> player.getInventory().setHelmet(extendedTargetPlayer.getSelectedKit().getHeadContent()), extendedTargetPlayer, 5 * 20);
            return true;
        }
        return false;
    }
}

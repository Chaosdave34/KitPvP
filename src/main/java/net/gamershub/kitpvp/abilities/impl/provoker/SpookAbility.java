package net.gamershub.kitpvp.abilities.impl.provoker;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpookAbility extends Ability {
    public SpookAbility() {
        super("spook", "Spook", AbilityType.RIGHT_CLICK, 30);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Places a pumpkin on the",
                "player you are looking at",
                "in a 20 block radius."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        Entity target = p.getTargetEntity(10);
        if (target instanceof Player player) {

                if (ExtendedPlayer.from(player).getGameState() == ExtendedPlayer.GameState.SPAWN)
                    return false;


            ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
            pumpkin.addEnchantment(Enchantment.BINDING_CURSE, 1);
            player.getInventory().setHelmet(pumpkin);

            Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, () -> {
                ExtendedPlayer extendedTargetPlayer = ExtendedPlayer.from(player);
                player.getInventory().setHelmet(extendedTargetPlayer.getSelectedKit().getHeadContent());
            }, 10 * 20);

            return true;
        }
        return false;
    }
}

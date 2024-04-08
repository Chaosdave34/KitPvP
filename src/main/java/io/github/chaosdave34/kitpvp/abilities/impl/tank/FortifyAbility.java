package io.github.chaosdave34.kitpvp.abilities.impl.tank;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.kits.Kit;
import io.github.chaosdave34.kitpvp.kits.impl.kits.TankKit;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FortifyAbility extends Ability {
    public FortifyAbility() {
        super("fortify", "Fortify", Type.RIGHT_CLICK, 30);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Upgrade your armor to enchanted",
                "netherite for 10 seconds, but also",
                "gain slowness."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        ExtendedPlayer extendedPlayer = ExtendedPlayer.from(p);
        PlayerInventory inventory = p.getInventory();

        if (extendedPlayer.getSelectedKitsKit() instanceof TankKit tankKit) {
            inventory.setHelmet(tankKit.getFortifiedHeadContent());
            inventory.setChestplate(tankKit.getFortifiedChestContent());
            inventory.setLeggings(tankKit.getFortifiedLegsContent());
            inventory.setBoots(tankKit.getFortifiedFeetContent());
        }

        new BukkitRunnable() {
            final int checkDeath = extendedPlayer.getTotalDeaths(ExtendedPlayer.GameType.KITS);

            @Override
            public void run() {
                if (checkDeath == extendedPlayer.getTotalDeaths(ExtendedPlayer.GameType.KITS)) {
                    Kit selectedKit = extendedPlayer.getSelectedKitsKit();
                    inventory.setHelmet(selectedKit.getHeadContent());
                    inventory.setChestplate(selectedKit.getChestContent());
                    inventory.setLeggings(selectedKit.getLegsContent());
                    inventory.setBoots(selectedKit.getFeetContent());
                }
            }
        }.runTaskLater(KitPvp.INSTANCE, 10 * 20);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 2, false, false, false));
        return true;
    }
}

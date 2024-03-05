package net.gamershub.kitpvp.abilities.impl.tank;

import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.gamershub.kitpvp.kits.Kit;
import net.gamershub.kitpvp.kits.impl.TankKit;
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
        super("fortify", "Fortify", AbilityType.RIGHT_CLICK, 30);
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
        ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(p);
        PlayerInventory inventory = p.getInventory();

        if (extendedPlayer.getSelectedKit() instanceof TankKit tankKit) {
            inventory.setHelmet(tankKit.getFortifiedHeadContent());
            inventory.setChestplate(tankKit.getFortifiedChestContent());
            inventory.setLeggings(tankKit.getFortifiedLegsContent());
            inventory.setBoots(tankKit.getFortifiedFeetContent());
        }

        new BukkitRunnable() {
            final int checkDeath = extendedPlayer.getTotalDeaths();

            @Override
            public void run() {
                if (checkDeath == extendedPlayer.getTotalDeaths()) {
                    Kit selectedKit = extendedPlayer.getSelectedKit();
                    inventory.setHelmet(selectedKit.getHeadContent());
                    inventory.setChestplate(selectedKit.getChestContent());
                    inventory.setLeggings(selectedKit.getLegsContent());
                    inventory.setBoots(selectedKit.getFeetContent());
                }
            }
        }.runTaskLater(KitPvpPlugin.INSTANCE, 10 * 20);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 2, false, false, false));
        return true;
    }
}

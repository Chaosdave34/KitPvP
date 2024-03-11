package net.gamershub.kitpvp.items.impl.artilleryman;

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import net.gamershub.kitpvp.ExtendedPlayer;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.gamershub.kitpvp.kits.KitHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RocketLauncher extends CustomItem {
    public RocketLauncher() {
        super(Material.CROSSBOW, "rocket_launcher", false);
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Rocket Launcher");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.ENHANCE);
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addEnchantment(Enchantment.MULTISHOT, 1);
        itemStack.addEnchantment(Enchantment.QUICK_CHARGE, 3);

        setCustomModelData(itemStack, 1);
    }

    @EventHandler
    public void onCrossbowLoad(EntityLoadCrossbowEvent e) {
        if (e.getEntity() instanceof Player player) {
            ExtendedPlayer extendedPlayer = KitPvpPlugin.INSTANCE.getExtendedPlayer(player);

            Bukkit.getScheduler().runTaskLater(KitPvpPlugin.INSTANCE, () -> {
                if (extendedPlayer.getSelectedKit() == KitHandler.ARTILLERYMAN) {
                    ItemStack crossbow = e.getCrossbow();
                    CrossbowMeta crossbowMeta = (CrossbowMeta) crossbow.getItemMeta();

                    List<ItemStack> projectiles = new ArrayList<>();

                    for (ItemStack projectile : crossbowMeta.getChargedProjectiles()) {
                        if (projectile.getType() == Material.FIREWORK_ROCKET) {
                            projectile.editMeta(FireworkMeta.class, fireworkMeta -> {
                                Random random = new Random();
                                Color randomColor = Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                                FireworkEffect.Type randomType = FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)];

                                fireworkMeta.addEffect(FireworkEffect.builder().withColor(randomColor).with(randomType).build());
                            });
                            projectiles.add(projectile);
                        }
                    }
                    crossbowMeta.setChargedProjectiles(projectiles);
                    crossbow.setItemMeta(crossbowMeta);
                }
            }, 1);
            e.setConsumeItem(false);
        }
    }
}

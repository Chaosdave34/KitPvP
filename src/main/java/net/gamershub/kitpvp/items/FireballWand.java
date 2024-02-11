package net.gamershub.kitpvp.items;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FireballWand extends CustomItem {
    public FireballWand() {
        super(Material.FIRE_CHARGE, "fireball_wand", false);
    }

    @Override
    public @NotNull Component getName() {
        return Component.text("Fireball Wand").decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        List<Component> description = new ArrayList<>();
        description.add(Component.text("Handle with care... ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        description.add(Component.text("or not, if you're feeling hot-headed!", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        return description;
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.FIREBALL);
    }

    @EventHandler
    public void onFireChargeUse(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() == null) return;
            PersistentDataContainer container = e.getItem().getItemMeta().getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(KitPvpPlugin.INSTANCE, "id");
            if (container.has(key)) {
                if (id.equals(container.get(key, PersistentDataType.STRING))) {
                    e.setCancelled(true);
                }
            }
        }
    }

}

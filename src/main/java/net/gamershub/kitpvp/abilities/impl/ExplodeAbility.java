package net.gamershub.kitpvp.abilities.impl;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.persistentdatatype.StringArrayPersistentDataType;
import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExplodeAbility extends Ability {
    public ExplodeAbility() {
        super("explode", "Explode", AbilityType.SNEAK, 10);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return List.of(Component.text("Cause an explosion.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public boolean onAbility(Player p) {
        p.getLocation().createExplosion(p, 5, false);
        return true;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof Player p) {
            for (ItemStack armorContent : p.getInventory().getArmorContents()) {
                if (armorContent == null) continue;

                PersistentDataContainer container = armorContent.getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(KitPvpPlugin.INSTANCE, "abilities");

                if (container.has(key)) {
                    String[] abilities = container.get(key, new StringArrayPersistentDataType());
                    if (abilities == null) return;

                    for (String id : abilities) {
                        if (this.id.equals(id)) {
                            e.blockList().removeIf(block -> !block.hasMetadata("placed_by_player"));
                            return;
                        }
                    }

                }
            }
        }
    }
}
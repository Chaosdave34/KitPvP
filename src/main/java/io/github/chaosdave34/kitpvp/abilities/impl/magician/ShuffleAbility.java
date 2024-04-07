package io.github.chaosdave34.kitpvp.abilities.impl.magician;

import io.github.chaosdave34.kitpvp.abilities.Ability;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleAbility extends Ability {
    public ShuffleAbility() {
        super("shuffle", "Shuffle", Type.SNEAK_RIGHT_CLICK, 5);
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription(
                "Shuffles the inventory of all players",
                "in a 5 block radius."
        );
    }

    @Override
    public boolean onAbility(Player p) {
        for (Entity entity : p.getNearbyEntities(5, 5, 5)) {
            if (entity instanceof Player target) {
                PlayerInventory inventory = target.getInventory();
                List<ItemStack> shuffledContent = new ArrayList<>(List.of(inventory.getContents()));
                Collections.shuffle(shuffledContent);
                inventory.setContents(shuffledContent.toArray(new ItemStack[]{}));
            }
        }
        return true;
    }
}

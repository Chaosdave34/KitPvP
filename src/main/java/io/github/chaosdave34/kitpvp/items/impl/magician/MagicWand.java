package io.github.chaosdave34.kitpvp.items.impl.magician;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MagicWand extends CustomItem {
    public MagicWand() {
        super(Material.STICK, "magic_wand", false);
    }


    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Magic Wand");
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Feel the magic flow through your body.");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.DEBUFF, AbilityHandler.LEVITATE, AbilityHandler.MAGIC_ATTACK);
    }


    @Override
    protected void additionalModifications(ItemStack itemStack) {
        setCustomModelData(itemStack, 1);
    }
}

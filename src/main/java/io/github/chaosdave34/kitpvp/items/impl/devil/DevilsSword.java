package io.github.chaosdave34.kitpvp.items.impl.devil;

import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DevilsSword extends CustomItem {
    public DevilsSword(){
        super(Material.IRON_SWORD, "devils_sword");
    }

    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Devil's Sword");
    }

    @Override
    protected void additionalModifications(ItemStack itemStack) {
        itemStack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
        setCustomModelData(itemStack, 1);
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.FIRE_STORM);
    }
}

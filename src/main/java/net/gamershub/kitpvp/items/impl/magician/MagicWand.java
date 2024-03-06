package net.gamershub.kitpvp.items.impl.magician;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.gamershub.kitpvp.items.CustomItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
        itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 8);
        setCustomModelData(itemStack, 1);
    }
}

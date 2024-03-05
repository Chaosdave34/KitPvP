package net.gamershub.kitpvp.enchantments;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

@Getter
public class CustomEnchantment extends Enchantment implements Listener {
    protected String id;
    protected int maxLevel;

    public CustomEnchantment(String id, int maxLevel, String displayName, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
        super(Rarity.COMMON, category, equipmentSlots);
        this.id = id;
        this.maxLevel = maxLevel;
        this.descriptionId = displayName;
    }

    @Override
    public @NotNull Component getFullname(int level) {
        return Component.translatable(this.getDescriptionId())
                .withStyle(ChatFormatting.BLUE)
                .append(CommonComponents.SPACE)
                .append(Component.translatable("enchantment.level." + level));
    }

}

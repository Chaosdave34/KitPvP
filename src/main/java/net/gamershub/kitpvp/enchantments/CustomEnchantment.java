package net.gamershub.kitpvp.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.event.Listener;

public class CustomEnchantment extends Enchantment implements Listener {
    protected String name;
    protected int maxLevel;
    protected String descriptionId;

    public CustomEnchantment(String name, int maxLevel, String descriptionId) {
        super(Rarity.COMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        this.name = name;
        this.maxLevel = maxLevel;
        this.descriptionId = descriptionId;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public String getDescriptionId() {
        return descriptionId;
    }
}

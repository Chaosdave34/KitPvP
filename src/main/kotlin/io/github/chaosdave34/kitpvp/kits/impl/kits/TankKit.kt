package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

class TankKit : Kit("tank", "Tank") {

    override fun getMaxHealth(): Double = 40.0

    override fun getHeadContent(): ItemStack = ItemStack(Material.DIAMOND_HELMET)

    override fun getChestContent(): ItemStack = ItemStack(Material.DIAMOND_CHESTPLATE)

    override fun getLegsContent(): ItemStack = ItemStack(Material.DIAMOND_LEGGINGS)

    override fun getFeetContent(): ItemStack = CustomItemHandler.TANK_BOOTS.build()

    fun getFortifiedHeadContent(): ItemStack {
        val netheriteHelmet = ItemStack(Material.NETHERITE_HELMET)
        netheriteHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        netheriteHelmet.editMeta { itemMeta: ItemMeta ->
            itemMeta.addAttributeModifier(
                Attribute.GENERIC_KNOCKBACK_RESISTANCE, AttributeModifier(
                    UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD
                )
            )
        }
        return netheriteHelmet
    }

    fun getFortifiedChestContent(): ItemStack {
        val netheriteChestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
        netheriteChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        netheriteChestplate.editMeta { itemMeta: ItemMeta ->
            itemMeta.addAttributeModifier(
                Attribute.GENERIC_KNOCKBACK_RESISTANCE,
                AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST)
            )
        }
        return netheriteChestplate
    }

    fun getFortifiedLegsContent(): ItemStack {
        val netheriteLeggings = ItemStack(Material.NETHERITE_LEGGINGS)
        netheriteLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        netheriteLeggings.editMeta { itemMeta: ItemMeta ->
            itemMeta.addAttributeModifier(
                Attribute.GENERIC_KNOCKBACK_RESISTANCE,
                AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS)
            )
        }

        return netheriteLeggings
    }

    fun getFortifiedFeetContent(): ItemStack {
        val netheriteBoots = ItemStack(Material.NETHERITE_BOOTS)
        netheriteBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        netheriteBoots.editMeta { itemMeta: ItemMeta ->
            itemMeta.addAttributeModifier(
                Attribute.GENERIC_KNOCKBACK_RESISTANCE,
                AttributeModifier(UUID.randomUUID(), "tank_kit", 0.1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET)
            )
        }
        return netheriteBoots
    }

    override fun getOffhandContent(): ItemStack = ItemStack(Material.SHIELD)

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.TANK_AXE.build(),
            ItemStack(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        return arrayOf(
            ItemStack(Material.GOLDEN_APPLE, 2),
            ItemStack(Material.COBBLED_DEEPSLATE, 32),
        )
    }
}
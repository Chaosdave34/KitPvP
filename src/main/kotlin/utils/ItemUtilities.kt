package utils

import org.bukkit.Color
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta

interface ItemUtilities {
    fun setLeatherArmorColor(leatherArmor: ItemStack, color: Color?) {
        leatherArmor.editMeta(LeatherArmorMeta::class.java) { leatherArmorMeta: LeatherArmorMeta ->
            leatherArmorMeta.setColor(
                color
            )
        }
    }

    fun setCustomModelData(itemStack: ItemStack, customModelData: Int?) {
        itemStack.editMeta { itemMeta: ItemMeta -> itemMeta.setCustomModelData(customModelData) }
    }
}
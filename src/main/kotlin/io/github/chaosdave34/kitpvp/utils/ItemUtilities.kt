package io.github.chaosdave34.kitpvp.utils

import org.bukkit.Color
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta

interface ItemUtilities {
    fun ItemStack.setLeatherArmorColor(color: Color?) {
        this.editMeta(LeatherArmorMeta::class.java) { leatherArmorMeta: LeatherArmorMeta -> leatherArmorMeta.setColor(color) }
    }

    fun ItemStack.setCustomModelData(customModelData: Int?) {
        this.editMeta { itemMeta: ItemMeta -> itemMeta.setCustomModelData(customModelData) }
    }
}
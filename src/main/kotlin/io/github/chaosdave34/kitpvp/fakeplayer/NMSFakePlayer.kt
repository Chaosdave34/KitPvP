package io.github.chaosdave34.kitpvp.fakeplayer

import com.mojang.authlib.GameProfile
import com.mojang.datafixers.util.Pair
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ClientInformation
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack

class NMSFakePlayer(server: MinecraftServer, world: ServerLevel, profile: GameProfile, clientOptions: ClientInformation) :
    ServerPlayer(server, world, profile, clientOptions) {

    lateinit var serverEntity: ServerEntity

    fun getEquipment(): List<Pair<EquipmentSlot, ItemStack>> {
        val equipment: MutableList<Pair<EquipmentSlot, ItemStack>> = mutableListOf()
        equipment.add(Pair(EquipmentSlot.MAINHAND, inventory.getSelected()))
        equipment.add(Pair(EquipmentSlot.OFFHAND, inventory.offhand[0]))

        equipment.add(Pair(EquipmentSlot.FEET, inventory.getArmor(0)))
        equipment.add(Pair(EquipmentSlot.LEGS, inventory.getArmor(1)))
        equipment.add(Pair(EquipmentSlot.CHEST, inventory.getArmor(2)))
        equipment.add(Pair(EquipmentSlot.HEAD, inventory.getArmor(3)))
        
        return equipment
    }
}
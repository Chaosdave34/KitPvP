package io.github.chaosdave34.kitpvp

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.world.entity.Entity
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.jetbrains.annotations.ApiStatus.Obsolete

object Utils {
    @JvmStatic
    fun registerEvents(listener: Listener) {
        KitPvp.INSTANCE.server.pluginManager.registerEvents(listener, KitPvp.INSTANCE)
    }

    @Obsolete
    fun spawnNmsEntity(entity: Entity) {
        Bukkit.getOnlinePlayers().forEach { player: Player -> spawnNmsEntity(player, entity) }
    }

    @JvmStatic
    @Obsolete
    fun spawnNmsEntity(p: Player, entity: Entity) {
        val cp = p as CraftPlayer
        val sp = cp.handle
        val connection = sp.connection

        val addEntityPacket = ClientboundAddEntityPacket(entity, entity.tracker!!.serverEntity)
        connection.send(addEntityPacket)

        val nonDefaultValues = entity.entityData.nonDefaultValues
        if (nonDefaultValues != null) {
            val setEntityDataPacket = ClientboundSetEntityDataPacket(entity.id, nonDefaultValues)
            connection.send(setEntityDataPacket)
        }
    }
}
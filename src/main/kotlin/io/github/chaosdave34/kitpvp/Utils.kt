package io.github.chaosdave34.kitpvp

import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.world.entity.Entity
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.Listener

object Utils {
    fun sendPacketsToOnlinePlayers(vararg packets: Packet<*>) {
        for (player in Bukkit.getOnlinePlayers()) {
            sendPacketsToPlayer(player, *packets)
        }
    }

    fun sendPacketsToPlayer(player: Player, vararg packets: Packet<*>) {
        val connection = (player as CraftPlayer).handle.connection
        packets.forEach { connection.send(it) }
    }

    @JvmStatic
    fun registerEvents(listener: Listener) {
        KitPvp.INSTANCE.server.pluginManager.registerEvents(listener, KitPvp.INSTANCE)
    }

    @Deprecated("")
    fun spawnNmsEntity(entity: Entity) {
        Bukkit.getOnlinePlayers().forEach { player: Player -> spawnNmsEntity(player, entity) }
    }

    @JvmStatic
    @Deprecated("")
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
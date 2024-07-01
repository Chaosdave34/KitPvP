package io.github.chaosdave34.kitpvp.extensions

import net.minecraft.network.protocol.Packet
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player


fun Player.sendPackets(vararg packets: Packet<*>) {
    val connection = (this as CraftPlayer).handle.connection
    packets.forEach { connection.send(it) }
}

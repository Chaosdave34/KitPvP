package io.github.chaosdave34.kitpvp.extensions

import io.github.chaosdave34.kitpvp.guis.Gui
import net.minecraft.network.protocol.Packet
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player


fun Player.sendPackets(vararg packets: Packet<*>) {
    val connection = (this as CraftPlayer).handle.connection
    packets.forEach { connection.send(it) }
}

fun Player.openGui(gui: Gui, arguments: Any? = null) {
    gui.open(this, arguments)
}

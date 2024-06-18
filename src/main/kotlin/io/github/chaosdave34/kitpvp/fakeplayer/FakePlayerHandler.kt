package io.github.chaosdave34.kitpvp.fakeplayer

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent
import com.mojang.authlib.GameProfile
import io.github.chaosdave34.kitpvp.Utils
import io.netty.channel.embedded.EmbeddedChannel
import net.minecraft.network.Connection
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.game.*
import net.minecraft.server.level.ClientInformation
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.entity.player.ChatVisiblity
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import java.net.InetSocketAddress
import java.util.*
import java.util.function.Consumer

class FakePlayerHandler : Listener {
    private val fakePlayers: MutableMap<Int, FakePlayer> = mutableMapOf()

    private fun createFakePlayer(world: World, profileName: String): NMSFakePlayer {
        val server = (Bukkit.getServer() as CraftServer).server
        val level = (world as CraftWorld).handle.level
        val profile = GameProfile(UUID.randomUUID(), profileName)
        val clientOptions =
            ClientInformation("en_us", 2, ChatVisiblity.FULL, true, 127, net.minecraft.world.entity.player.Player.DEFAULT_MAIN_HAND, false, false)

        val fakePlayer = NMSFakePlayer(server, level, profile, clientOptions)

        val connection = Connection(PacketFlow.SERVERBOUND)
        connection.channel = EmbeddedChannel()
        connection.address = InetSocketAddress("localhost", 0)
        ServerGamePacketListenerImpl(server, connection, fakePlayer, CommonListenerCookie(profile, 0, clientOptions, false))

        val trackedPlayers = Bukkit.getOnlinePlayers().map { (it as CraftPlayer).handle.connection }.toSet()
        val serverEntity = ServerEntity(level, fakePlayer, 1, true, { Utils.sendPacketsToOnlinePlayers(it) }, trackedPlayers)

        fakePlayer.serverEntity = serverEntity

        return fakePlayer
    }

    fun spawnFakePlayer(world: World, location: Location, name: String, function: Consumer<FakePlayer>): FakePlayer {
        val nmsFakePlayer = createFakePlayer(world, name)
        nmsFakePlayer.setPos(location.x, location.y, location.z)
        nmsFakePlayer.setRot(location.yaw, location.pitch)
        nmsFakePlayer.setYHeadRot(location.yaw)

        val fakePlayer = FakePlayer(Bukkit.getServer() as CraftServer, nmsFakePlayer)
        function.accept(fakePlayer)

        fakePlayers[fakePlayer.entityId] = fakePlayer

        showFakePlayerToOnline(fakePlayer)

        return fakePlayer
    }

    fun removeFakePlayer(fakePlayer: FakePlayer) {
        fakePlayers.remove(fakePlayer.entityId)

        val removePlayerPacket = ClientboundPlayerInfoRemovePacket(listOf(fakePlayer.uniqueId))
        val removeEntitiesPacket = ClientboundRemoveEntitiesPacket(fakePlayer.entityId)

        Utils.sendPacketsToOnlinePlayers(removePlayerPacket, removeEntitiesPacket)
    }

    fun showFakePlayerToOnline(fakePlayer: FakePlayer) {
        val nmsFakePlayer = fakePlayer.handle
        val serverEntity = nmsFakePlayer.serverEntity
        val addPlayerPacket = ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsFakePlayer)
        val spawnEntityPacket = ClientboundAddEntityPacket(nmsFakePlayer, serverEntity)
        val setEquipmentPacket = ClientboundSetEquipmentPacket(nmsFakePlayer.id, nmsFakePlayer.getEquipment())

        Bukkit.getOnlinePlayers().forEach { player ->
            if (player.canSee(fakePlayer)) {
                Utils.sendPacketsToPlayer(player, addPlayerPacket, spawnEntityPacket, setEquipmentPacket)
            }
        }
    }

    fun showAllVisibleFakePlayer(player: Player) {
        fakePlayers.forEach { showFakePlayer(it.value, player) }
    }

    private fun showFakePlayer(fakePlayer: FakePlayer, player: Player) {
        val nmsFakePlayer = fakePlayer.handle
        val serverEntity = nmsFakePlayer.serverEntity

        if (player.canSee(fakePlayer) && player.world == fakePlayer.world) {
            val addPlayerPacket = ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsFakePlayer)
            val spawnEntityPacket = ClientboundAddEntityPacket(nmsFakePlayer, serverEntity)
            val setEquipmentPacket = ClientboundSetEquipmentPacket(nmsFakePlayer.id, nmsFakePlayer.getEquipment())

            Utils.sendPacketsToPlayer(player, addPlayerPacket, spawnEntityPacket, setEquipmentPacket)
            serverEntity.sendChanges()
        }
    }

    @EventHandler
    fun onFakePlayerInteract(event: PlayerUseUnknownEntityEvent) {
        val fakePlayer = fakePlayers[event.entityId]
        fakePlayer?.onInteract(PlayerUseFakePlayerEvent(fakePlayer, event))

    }

    @EventHandler
    fun onPlayerWorldChange(event: PlayerChangedWorldEvent) {
        showAllVisibleFakePlayer(event.player)
    }
}
package io.github.chaosdave34.kitpvp.fakeplayer

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent
import com.mojang.authlib.GameProfile
import io.github.chaosdave34.kitpvp.extensions.sendPackets
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
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
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

        val serverEntity = ServerEntity(level, fakePlayer, 1, true, { packet -> Bukkit.getOnlinePlayers().forEach { it.sendPackets(packet) } }, setOf())
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

        Bukkit.getOnlinePlayers().forEach { it.sendPackets(removePlayerPacket, removeEntitiesPacket) }
    }

    private fun showFakePlayerToOnline(fakePlayer: FakePlayer) {
        val nmsFakePlayer = fakePlayer.handle
        val serverEntity = nmsFakePlayer.serverEntity

        val addPlayerPacket = ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsFakePlayer)
        val spawnEntityPacket = ClientboundAddEntityPacket(nmsFakePlayer, serverEntity)
        val setEquipmentPacket = ClientboundSetEquipmentPacket(nmsFakePlayer.id, nmsFakePlayer.getEquipment())

        Bukkit.getOnlinePlayers().forEach { player ->
            if (player.canSee(fakePlayer) && player.world == fakePlayer.world) {
                player.sendPackets(addPlayerPacket, spawnEntityPacket, setEquipmentPacket)
            }
        }
        serverEntity.sendChanges()
    }

    private fun showAllVisibleFakePlayer(player: Player) {
        fakePlayers.forEach { showFakePlayer(it.value, player) }
    }

    private fun showFakePlayer(fakePlayer: FakePlayer, player: Player) {
        val nmsFakePlayer = fakePlayer.handle
        val serverEntity = nmsFakePlayer.serverEntity

        if (player.canSee(fakePlayer) && player.world == fakePlayer.world) {
            val addPlayerPacket = ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsFakePlayer)
            val spawnEntityPacket = ClientboundAddEntityPacket(nmsFakePlayer, serverEntity)
            val entityDataPacket = ClientboundSetEntityDataPacket(nmsFakePlayer.id, nmsFakePlayer.entityData.nonDefaultValues ?: listOf())
            val setEquipmentPacket = ClientboundSetEquipmentPacket(nmsFakePlayer.id, nmsFakePlayer.getEquipment())

            player.sendPackets(addPlayerPacket, spawnEntityPacket, entityDataPacket, setEquipmentPacket)
        }
    }

    @EventHandler
    fun onFakePlayerInteract(event: PlayerUseUnknownEntityEvent) {
        val fakePlayer = fakePlayers[event.entityId]
        fakePlayer?.onInteract(PlayerUseFakePlayerEvent(fakePlayer, event))

    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        showAllVisibleFakePlayer(event.player)
    }

    @EventHandler
    fun onPlayerWorldChange(event: PlayerChangedWorldEvent) {
        showAllVisibleFakePlayer(event.player)
    }
}
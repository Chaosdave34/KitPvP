package io.github.chaosdave34.kitpvp.textdisplays

import io.github.chaosdave34.kitpvp.extensions.sendPackets
import io.papermc.paper.adventure.PaperAdventure
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerEntity
import net.minecraft.world.entity.Display
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.function.Consumer

class TextDisplayHandler : Listener {
    private val textDisplays: MutableMap<Int, CustomTextDisplay> = mutableMapOf()

    private fun createTextDisplay(world: World): Display.TextDisplay {
        val level = (world as CraftWorld).handle.level

        val textDisplay = NMSCustomTextDisplay(level)

        val serverEntity = ServerEntity(level, textDisplay, 1, true, { packet -> Bukkit.getOnlinePlayers().forEach { it.sendPackets(packet) } }, setOf())
        textDisplay.serverEntity = serverEntity

        return textDisplay
    }

    fun spawnTextDisplay(world: World, location: Location, function: Consumer<CustomTextDisplay>): CustomTextDisplay {
        val nmsTextDisplay = createTextDisplay(world)
        nmsTextDisplay.setPos(location.x, location.y, location.z)
        nmsTextDisplay.setRot(location.yaw, location.pitch)

        val textDisplay = CustomTextDisplay(Bukkit.getServer() as CraftServer, nmsTextDisplay)
        function.accept(textDisplay)

        textDisplays[textDisplay.entityId] = textDisplay

        showTextDisplayToOnline(textDisplay)

        return textDisplay
    }

    fun removeTextDisplay(textDisplay: TextDisplay) {
        textDisplays.remove(textDisplay.entityId)

        val removeEntitiesPacket = ClientboundRemoveEntitiesPacket(textDisplay.entityId)

        Bukkit.getOnlinePlayers().forEach { it.sendPackets(removeEntitiesPacket) }
    }

    fun showTextDisplayToOnline(textDisplay: CustomTextDisplay) {
        val nmsTextDisplay = textDisplay.handle
        val serverEntity = nmsTextDisplay.serverEntity

        val spawnEntityPacket = ClientboundAddEntityPacket(nmsTextDisplay, serverEntity)

        Bukkit.getOnlinePlayers().forEach { player ->
            if (player.canSee(textDisplay) && player.world == textDisplay.world) {
                Bukkit.getOnlinePlayers().forEach { it.sendPackets(spawnEntityPacket) }
            }
        }
        serverEntity.sendChanges()
    }

    private fun showTextDisplay(textDisplay: CustomTextDisplay, player: Player) {
        val nmsTextDisplay = textDisplay.handle
        val serverEntity = nmsTextDisplay.serverEntity

        if (player.canSee(textDisplay) && player.world == textDisplay.world) {
            val spawnEntityPacket = ClientboundAddEntityPacket(nmsTextDisplay, serverEntity)

            val entityData = nmsTextDisplay.entityData.nonDefaultValues ?: mutableListOf()
            entityData.add(SynchedEntityData.DataValue(23, EntityDataSerializers.COMPONENT, PaperAdventure.asVanilla(textDisplay.text(player))))
            val entityDataPacket = ClientboundSetEntityDataPacket(nmsTextDisplay.id, entityData)

            player.sendPackets(spawnEntityPacket, entityDataPacket)
        }
    }

    private fun showAllVisibleTextDisplays(player: Player) {
        textDisplays.forEach { showTextDisplay(it.value, player) }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        showAllVisibleTextDisplays(event.player)
    }

    @EventHandler
    fun onPlayerWorldChange(event: PlayerChangedWorldEvent) {
        showAllVisibleTextDisplays(event.player)
    }
}
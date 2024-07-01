package io.github.chaosdave34.kitpvp.textdisplays

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.extensions.sendPackets
import io.papermc.paper.adventure.PaperAdventure
import io.papermc.paper.annotation.DoNotUse
import net.kyori.adventure.text.Component
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Display.TextDisplay
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.entity.CraftTextDisplay
import org.bukkit.entity.Player
import java.util.*

open class CustomTextDisplay(server: CraftServer, entity: TextDisplay) : CraftTextDisplay(server, entity) {
    var defaultText: Component = Component.empty()
    private val perPlayerText: MutableMap<UUID, Component> = mutableMapOf()

    override fun getHandle(): NMSCustomTextDisplay {
        return entity as NMSCustomTextDisplay
    }

    override fun remove() {
        super.remove()
        KitPvp.INSTANCE.textDisplayHandler.removeTextDisplay(this)
    }

    @DoNotUse
    @Deprecated("Replaced by defaultText", ReplaceWith("defaultText = text"))
    override fun text(text: Component?) {
        if (text != null) defaultText = text
    }

    fun text(text: Component, player: Player) {
        perPlayerText[player.uniqueId] = text
    }

    @DoNotUse
    @Deprecated("Replaced by defaultText", ReplaceWith("defaultText"))
    override fun text(): Component {
        return defaultText
    }

    fun text(player: Player): Component {
        return perPlayerText[player.uniqueId] ?: defaultText
    }

    fun updateText() {
        updateText(*Bukkit.getOnlinePlayers().toTypedArray())
    }

    fun <T : Player> updateText(vararg player: T) {
        player.forEach { updateText(it) }
    }


    fun updateText(player: Player) {
        val entityData = handle.entityData.nonDefaultValues ?: mutableListOf()

        Bukkit.getLogger().info(entityData.toString())

        entityData.add(SynchedEntityData.DataValue(23, EntityDataSerializers.COMPONENT, PaperAdventure.asVanilla(text(player))))

        val entityDataPacket = ClientboundSetEntityDataPacket(handle.id, entityData)

        player.sendPackets(entityDataPacket)
    }
}
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
import org.bukkit.entity.Display
import org.bukkit.entity.Player

open class CustomTextDisplay(server: CraftServer, entity: TextDisplay) : CraftTextDisplay(server, entity) {
    var defaultText: Component = Component.empty()
    var dynamicText: Function1<Player, Component>? = null

    init {
        // New default values
        billboard = Display.Billboard.VERTICAL
        isSeeThrough = true
    }

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

    @DoNotUse
    @Deprecated("Replaced by defaultText", ReplaceWith("defaultText"))
    override fun text(): Component {
        return defaultText
    }

    fun text(player: Player):Component {
        return dynamicText?.invoke(player) ?: defaultText
    }

    fun updateTextForAll() {
        updateText(*Bukkit.getOnlinePlayers().toTypedArray())
    }

    fun  updateText(vararg players: Player) {
        players.forEach {
            val entityData = handle.entityData.nonDefaultValues ?: mutableListOf()
            entityData.add(SynchedEntityData.DataValue(23, EntityDataSerializers.COMPONENT, PaperAdventure.asVanilla(text(it))))

            val entityDataPacket = ClientboundSetEntityDataPacket(handle.id, entityData)
            it.sendPackets(entityDataPacket)
        }
    }


}
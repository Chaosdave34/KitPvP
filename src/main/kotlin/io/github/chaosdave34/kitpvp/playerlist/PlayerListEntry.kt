package io.github.chaosdave34.kitpvp.playerlist

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import io.github.chaosdave34.kitpvp.extensions.sendPackets
import io.papermc.paper.adventure.AdventureComponent
import net.kyori.adventure.text.Component
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket
import net.minecraft.world.level.GameType
import org.bukkit.entity.Player
import java.util.*

abstract class PlayerListEntry(val target: Player, val index: Int)  {

    fun create() {
        val gameProfile = GameProfile(UUID.randomUUID(), "${100 + index}")
        getTexture()?.let {
            gameProfile.properties.put("textures", Property("textures", it.first, it.second))
        }

        val entry =
            ClientboundPlayerInfoUpdatePacket.Entry(
                gameProfile.id,
                gameProfile,
                true,
                0,
                GameType.CREATIVE,
                AdventureComponent(getText()).deepConverted(),
                null
            )
        val addPlayerPacket = ClientboundPlayerInfoUpdatePacket(
            EnumSet.of(
                ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
            ), entry
        )
        target.sendPackets(addPlayerPacket)
    }

    abstract fun getText(): Component

    abstract fun getTexture(): Pair<String, String?>?
}
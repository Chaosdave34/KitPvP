package io.github.chaosdave34.kitpvp.playerlist

import com.destroystokyo.paper.profile.CraftPlayerProfile
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class PlayerListPlayerEntry(target: Player, val player: Player, number: Int) : PlayerListEntry(target, number) {
    override fun getText(): Component = player.displayName()

    override fun getTexture(): Pair<String, String?>? {
        val gameProfile = CraftPlayerProfile.asAuthlib(player.playerProfile)
        val property = gameProfile.properties["textures"].first() ?: return null
        return Pair(property.value, property.signature)
    }
}
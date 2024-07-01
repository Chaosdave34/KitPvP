package io.github.chaosdave34.kitpvp.textdisplays

import io.github.chaosdave34.kitpvp.extensions.createTextDisplay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Display

object TextDisplays {
    fun create(world: World) {

        if (world.name == "world") {
            // Static
            // Jump
            world.createTextDisplay(Location(null, 2.0, 120.0, 2.0)) {
                it.defaultText = Component.text("JUMP", NamedTextColor.GREEN, TextDecoration.BOLD)
                it.billboard = Display.Billboard.VERTICAL
            }.updateText()

            // Info
            world.createTextDisplay(Location(null, -1.5, 121.5, 11.5)) {
                val text = listOf(
                    Component.text("Info:", NamedTextColor.GOLD, TextDecoration.BOLD),
                    Component.text("- Use /spawn to respawn"),
                    Component.text("- Placed blocks disappear"),
                    Component.text("after 45 seconds"),
                    Component.text("- Killing a player rewards you with"),
                    Component.text("consumable items (depending on your kit)"),
                    Component.text("- You gain XP and coins for killing players")
                )

                it.defaultText = Component.join(JoinConfiguration.newlines(), text)
                it.billboard = Display.Billboard.VERTICAL
            }

            // Amusement Park
            world.createTextDisplay(Location(null, -11.0, 120.5, 15.0)) {
                it.defaultText = Component.text("Amusement Park").decorate(TextDecoration.BOLD)
                    .append(Component.newline())
                    .append(Component.text("(whitelisted)"))

                it.billboard = Display.Billboard.VERTICAL
            }

        }

    }
}
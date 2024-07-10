package io.github.chaosdave34.kitpvp.guis

import io.github.chaosdave34.kitpvp.KitPvp
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class Gui(private val defaultId: String) {
    val pages: MutableMap<String, Pair<Page, Function3<Page, Player, Any?, Unit>>> = mutableMapOf()

    init {
        KitPvp.INSTANCE.guiHandler.guis.add(this)
    }


    fun createDefaultPage(title: Component, rows: Int, function: Function3<Page, Player, Any?, Unit> = { _, _, _ -> }): Page =
        createPage(defaultId, title, rows, function)

    fun createPage(id: String, title: Component, rows: Int, function: Function3<Page, Player, Any?, Unit> = { _, _, _ -> }): Page {
        val page = Page(rows, title)
        pages[id] = Pair(page, function)
        return page
    }

    fun open(player: Player, arguments: Any? = null) = openPage(defaultId, player, arguments)


    fun openPage(id: String, player: Player, arguments: Any? = null) {
        val (page, function) = pages[id] ?: return

        page.eventHandlers.clear()

        function.invoke(page, player, arguments)
        player.openInventory(page.inventory)
    }
}
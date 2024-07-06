package io.github.chaosdave34.kitpvp.guis

import io.github.chaosdave34.kitpvp.KitPvp
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class Gui(private val defaultId: String) {
    val pages: MutableMap<String, Pair<Page, Function2<Page, Player, Unit>>> = mutableMapOf()

    init {
        KitPvp.INSTANCE.guiHandler.guis.add(this)
    }


    fun createDefaultPage(title: Component, rows: Int, function: Function2<Page, Player, Unit> = { _, _ -> }): Page =
        createPage(defaultId, title, rows, function)

    fun createPage(id: String, title: Component, rows: Int, function: Function2<Page, Player, Unit> = { _, _ -> }): Page {
        val page = Page(rows, title)
        pages[id] = Pair(page, function)
        return page
    }

    fun open(player: Player) = openPage(defaultId, player)


    fun openPage(id: String, player: Player) {
        val (page, function) = pages[id] ?: return

        page.eventHandlers.clear()

        function.invoke(page, player)
        player.openInventory(page.inventory)
    }
}
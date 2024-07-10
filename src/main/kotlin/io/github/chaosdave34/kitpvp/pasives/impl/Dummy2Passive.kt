package io.github.chaosdave34.kitpvp.pasives.impl

import io.github.chaosdave34.kitpvp.pasives.Passive
import net.kyori.adventure.text.Component
import org.bukkit.Material

class Dummy2Passive: Passive("dummy_2", "Dummy 2", Material.GRAY_DYE) {
    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Another dummy passive for testing purpose")
}
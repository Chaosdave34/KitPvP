package io.github.chaosdave34.kitpvp.pasives.impl

import io.github.chaosdave34.kitpvp.pasives.Passive
import net.kyori.adventure.text.Component
import org.bukkit.Material

class DummyPassive : Passive("dummy", "Dummy", Material.WHITE_DYE) {
    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Dummy passive for testing purpose")

}
package io.github.chaosdave34.kitpvp

import io.github.chaosdave34.kitpvp.commands.CommandBootstrap
import io.github.chaosdave34.kitpvp.enchantments.EnchantmentBootstrap
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import io.papermc.paper.registry.event.RegistryEvents

class PluginBootstrap : PluginBootstrap {
    override fun bootstrap(context: BootstrapContext) {
        context.lifecycleManager.registerEventHandler(RegistryEvents.ENCHANTMENT.freeze().newHandler { EnchantmentBootstrap.register(it) })

        context.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { CommandBootstrap.register(it) }
    }
}
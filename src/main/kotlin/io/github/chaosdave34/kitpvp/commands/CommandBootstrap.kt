package io.github.chaosdave34.kitpvp.commands

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent

object CommandBootstrap {

    fun register(event: ReloadableRegistrarEvent<Commands>) {
        val commands = event.registrar()

        SpawnCommand.register(commands)
        GommemodeCommand.register(commands)
        CustomItemCommand.register(commands)
        AddCoinsCommand.register(commands)
        BountyCommand.register(commands)
        AddExperienceCommand.register(commands)
        MsgCommand.register(commands)
        LoopCommand.register(commands)
        ProfileCommand.registerCommand(commands)
    }
}
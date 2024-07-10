package io.github.chaosdave34.kitpvp.pasives

import io.github.chaosdave34.kitpvp.pasives.impl.Dummy2Passive
import io.github.chaosdave34.kitpvp.pasives.impl.DummyPassive

class PassiveHandler {
    val passives: MutableMap<String, Passive> = mutableMapOf()

    init {
        registerPassive(DummyPassive())
        registerPassive(Dummy2Passive())
    }

    private fun registerPassive(passive: Passive): Passive {
        passives[passive.id] = passive
        return passive
    }
}
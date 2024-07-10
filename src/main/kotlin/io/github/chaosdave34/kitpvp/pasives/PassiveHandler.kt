package io.github.chaosdave34.kitpvp.pasives

import io.github.chaosdave34.kitpvp.pasives.impl.DummyPassive

class PassiveHandler {
    val passives: MutableMap<String, Passive> = mutableMapOf()

    init {
        registerPassive(DummyPassive())
    }

    private fun registerPassive(passive: Passive): Passive {
        passives[passive.id] = passive
        return passive
    }
}
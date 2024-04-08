package io.github.chaosdave34.kitpvp.entities

import io.github.chaosdave34.ghutils.entity.CustomEntity
import io.github.chaosdave34.kitpvp.entities.impl.Turret

class CustomEntities {
    companion object {
        @JvmField
        var TURRET: CustomEntity = Turret()
    }
}
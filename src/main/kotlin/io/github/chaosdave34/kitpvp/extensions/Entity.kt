package io.github.chaosdave34.kitpvp.extensions

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.persistentdatatypes.UUIDPersistentDataType
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import java.util.*

private val OWNER_KEY = NamespacedKey(KitPvp.INSTANCE, "owner")

fun Entity.setPDCOwner(uuid: UUID) {
    this.persistentDataContainer.set(OWNER_KEY, UUIDPersistentDataType(), uuid)
}

fun Entity.setPDCOwner(entity: LivingEntity) = this.setPDCOwner(entity.uniqueId)

fun Entity.getPDCOwner(): UUID? = this.persistentDataContainer.get(OWNER_KEY, UUIDPersistentDataType())


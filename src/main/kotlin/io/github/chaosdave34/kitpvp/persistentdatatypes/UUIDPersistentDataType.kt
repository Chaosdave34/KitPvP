package io.github.chaosdave34.kitpvp.persistentdatatypes

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.ByteBuffer
import java.util.*

class UUIDPersistentDataType : PersistentDataType<Array<Byte>, UUID> {
    override fun getPrimitiveType(): Class<Array<Byte>> = Array<Byte>::class.java

    override fun getComplexType(): Class<UUID> = UUID::class.java

    override fun fromPrimitive(primitive: Array<Byte>, context: PersistentDataAdapterContext): UUID {
        val bb = ByteBuffer.wrap(primitive.toByteArray())
        val firstLong = bb.getLong()
        val secondLong = bb.getLong()
        return UUID(firstLong, secondLong)
    }

    override fun toPrimitive(complex: UUID, context: PersistentDataAdapterContext): Array<Byte> {
        val byteBuffer = ByteBuffer.wrap(ByteArray(16))
        byteBuffer.putLong(complex.mostSignificantBits)
        byteBuffer.putLong(complex.leastSignificantBits)
        return byteBuffer.array().toTypedArray()
    }
}
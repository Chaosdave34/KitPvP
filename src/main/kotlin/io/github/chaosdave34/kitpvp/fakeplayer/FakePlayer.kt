package io.github.chaosdave34.kitpvp.fakeplayer

import com.mojang.authlib.properties.Property
import io.github.chaosdave34.kitpvp.KitPvp
import net.minecraft.server.level.ServerPlayer
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.entity.CraftPlayer
import java.util.function.Consumer

open class FakePlayer(server: CraftServer, entity: ServerPlayer) : CraftPlayer(server, entity) {
    private var interactionEventConsumer: Consumer<PlayerUseFakePlayerEvent>? = null

    override fun getHandle(): NMSFakePlayer {
        return entity as NMSFakePlayer
    }

    override fun remove() {
        super.remove()
        KitPvp.INSTANCE.fakePlayerHandler.removeFakePlayer(this)
    }

    fun setTexture(texture: String, signature: String) {
        profile.properties.put("textures", Property("textures", texture, signature))
    }

    fun updateEntityData() {
        handle.serverEntity.sendChanges()
    }

    fun onInteract(event: PlayerUseFakePlayerEvent) {
        if (event.isActualInteract || event.isAttack) interactionEventConsumer?.accept(event)

    }

    fun setInteractionEventConsumer(consumer: Consumer<PlayerUseFakePlayerEvent>) {
        interactionEventConsumer = consumer
    }

}
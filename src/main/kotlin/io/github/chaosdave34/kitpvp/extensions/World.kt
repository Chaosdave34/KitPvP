package io.github.chaosdave34.kitpvp.extensions

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.fakeplayer.FakePlayer
import org.bukkit.Location
import org.bukkit.World
import java.util.function.Consumer

fun World.spawnFakePlayer(location: Location, name: String, function: Consumer<FakePlayer>): FakePlayer =
    KitPvp.INSTANCE.fakePlayerHandler.spawnFakePlayer(this, location, name, function)

package io.github.chaosdave34.kitpvp.extensions

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.fakeplayer.FakePlayer
import io.github.chaosdave34.kitpvp.textdisplays.CustomTextDisplay
import org.bukkit.Location
import org.bukkit.World
import java.util.function.Consumer

fun World.createFakePlayer(location: Location, name: String, function: Consumer<FakePlayer> = Consumer {  }): FakePlayer =
    KitPvp.INSTANCE.fakePlayerHandler.spawnFakePlayer(this, location, name, function)

fun World.createTextDisplay(location: Location, function: Consumer<CustomTextDisplay> = Consumer {  }): CustomTextDisplay =
    KitPvp.INSTANCE.textDisplayHandler.spawnTextDisplay(this, location, function)
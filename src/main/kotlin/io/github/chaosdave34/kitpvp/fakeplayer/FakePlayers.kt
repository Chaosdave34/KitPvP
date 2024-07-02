package io.github.chaosdave34.kitpvp.fakeplayer

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.extensions.createFakePlayer
import io.github.chaosdave34.kitpvp.guis.Guis
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.inventory.ItemStack

object FakePlayers {

    fun create() {

        val world = Bukkit.getWorld("world")
        if (world != null) {
            // Elytra PvP
            world.createFakePlayer(Location(null, -1.5, 120.0, 6.5, -45f, 0f), "Elytra PvP") {
                it.equipment.chestplate = ItemStack(Material.ELYTRA)

                it.setInteractionEventConsumer { event ->
                    if (event.isActualInteract || event.isAttack) {
                        val extendedPlayer = ExtendedPlayer.from(event.player)

                        if (extendedPlayer.inSpawn()) extendedPlayer.spawn(ExtendedPlayer.GameType.ELYTRA)
                    }
                }
            }

            // Kit PvP Cosmetics
            world.createFakePlayer(Location(null, 4.5, 120.0, 12.5, 180f, 0f), "Cosmetics") {
                it.setInteractionEventConsumer { event ->
                    if (event.isActualInteract || event.isAttack) {
                        if (ExtendedPlayer.from(event.player).inSpawn()) Guis.COSMETICS.show(event.player)
                    }
                }
            }
        }

        val worldElytra = Bukkit.getWorld("world_elytra")
        if (worldElytra != null) {

            // Eclipse Photon
            worldElytra.createFakePlayer(Location(null, -69.5, 107.0, -14.5, -90f, 0f), "EclipsePhoton") {
                val texture =
                    "ewogICJ0aW1lc3RhbXAiIDogMTU5NDY3Mjg5NzYyNiwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2I5YTllMjUwOTY2YjE2NmYwYjY4ZWVmNGVmZjI1ZmJmNzg0YmQ3YjhhYjk5MzE0YzcyNWQ3ZTdkYzdkMzMxNTQiCiAgICB9CiAgfQp9"
                val textureSignature =
                    "FA+Kd+HuRBqkBJx0xK253TtP37txmvWQ2YzzJS6yZO2TC9saRF0WEDsp6XRx+mJfFX6YsIEdGjSuAYv8++KjhLrKfJNxzMGt4XuhPilbLbrTlsBcjyXWQK3qUXKhGfyx8HOTGBZwjSPcbHmEXeDQ4zSpNqMbvC41kv/718uiLKPAQoZvSB9+wDBGwl/bMBnd6j+irPMVRz3cQFHF4w8tSg5yuwdtod+abnp4KWanWFq5WXVMcicJvMn0UaQDlBH/3T51nh32y2VzLFdwGwUDkSCsFGnQNH7H8VLB1X5mNV4J95aIuqtCzaCeD3ST9uQevf+/pVrAKtUTvllcO5i+KU/8+r8esJWVtUxxdP6AThV4rddLN+PjVM4RLQ9nVLc2Z6c26VCjZlQ7aErt9MdNRNGBHNKTXMvfqeLBp4iS0HSlOpJeeTQ8trpOpAkVJvfxTq9/xmyFNE0uqQ0UwV2D+NWVBb+3GSl6cuvzxtjSrDClWa+qjF6A7gOAutNfVuubqdD5vMNliPRCbUMmBunZze43tTmKRiUaxpTNj8OoTM/FvFmiMJVzuLa9e9RduFkyZp/DJWZqGEWhs/VGM4kdiBDiJT/txrk9hTyAQZlyNLWX7GTvKkFzmUT0kR9bvKlvR1zRORZfZuHjyApEJpPyvjZfOdEmOC6J/7Ik4bnAkFw="

                it.setTexture(texture, textureSignature)

                it.setInteractionEventConsumer { event ->
                    if (event.isActualInteract || event.isAttack) event.player.sendMessage(Component.text("Ich werde euren Server zerstüren!"))
                }
            }

            // Kit PvP
            worldElytra.createFakePlayer(Location(null, 16.5, 200.0, -2.5, 10f, 0f), "Kit PvP") {
                it.setInteractionEventConsumer { event ->
                    if (event.isActualInteract || event.isAttack) {
                        val extendedPlayer = ExtendedPlayer.from(event.player)

                        if (extendedPlayer.inSpawn()) extendedPlayer.spawn(ExtendedPlayer.GameType.KITS)
                    }
                }
            }

            // Elytra Kit Selector
            worldElytra.createFakePlayer(Location(null, 10.5, 200.0, -5.5, 0f, 0f), "Kits") {
                it.setInteractionEventConsumer { event ->
                    if (event.isActualInteract || event.isAttack) {
                        if (ExtendedPlayer.from(event.player).inSpawn()) Guis.ELYTRA_KITS.show(event.player)
                    }
                }
            }

            // Elytra PvP Cosmetics
            worldElytra.createFakePlayer(Location(null, 16.5, 200.0, 4.5, 135f, 0f), "Cosmetics") {
                it.setInteractionEventConsumer { event ->
                    if (event.isActualInteract || event.isAttack) {
                        if (ExtendedPlayer.from(event.player).inSpawn()) Guis.COSMETICS.show(event.player)
                    }
                }
            }

        }
    }
}
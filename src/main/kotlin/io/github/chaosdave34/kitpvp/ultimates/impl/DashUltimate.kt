package io.github.chaosdave34.kitpvp.ultimates.impl

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.AbilityRunnable
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import org.bukkit.Material
import org.bukkit.entity.Horse
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DashUltimate : Ultimate("dash", "Dash", 30, 5.0, Material.CHAIN) {
    override fun getDescription() = createSimpleDescriptionAsList("Dash forward damaging enemies.")


    override fun onAbility(player: Player): Boolean { // Todo: Damage and knock back

        val horse = player.location.world.spawn(player.location, Horse::class.java) { horse ->
            horse.owner = player
            horse.isInvulnerable = true
        }
        horse.addPassenger(player)
        val direction = player.eyeLocation.direction.multiply(0.5)
        horse.velocity = direction
        val content = player.inventory.storageContents
        player.inventory.storageContents = arrayOf()
        player.inventory.addItem(ItemStack.of(Material.DIAMOND_SWORD))

        object : AbilityRunnable(player) {
            override fun runInGame() {

                for (entity in player.getNearbyEntities(3.0, 3.5, 3.0)) {
                    if (entity is LivingEntity) {
                        entity.velocity = direction.multiply(2)
                        entity.damage(7.5, player)
                    }
                }   
                if (i > 1.5 * 2) {
                    cancel()
                }
            }
        }.runTaskTimer(KitPvp.INSTANCE, 1, 10)

        AbilityRunnable.runTaskLater(KitPvp.INSTANCE, { horse.remove() }, player, 20 * 20)

        AbilityRunnable.runTaskLater(KitPvp.INSTANCE, { player.inventory.storageContents = content }, player, 20 * 20)

        return true
    }

}































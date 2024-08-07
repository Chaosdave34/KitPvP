package io.github.chaosdave34.kitpvp.items.impl.magician

import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler
import io.github.chaosdave34.kitpvp.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

class MagicWand : CustomItem(Material.END_ROD, "magic_wand", "Magic Wand", false, true) {
    override fun getDescription(): List<Component> = createSimpleDescriptionAsList("Feel the magic flow through cour body.")

    override fun getAbilities(): List<Ability> = listOf(AbilityHandler.SHUFFLE)

    override fun additionalModifications(itemStack: ItemStack) {
        itemStack.setCustomModelData(1)
        itemStack.editMeta { itemMeta ->
            itemMeta.addAttributeModifier(
                Attribute.GENERIC_ATTACK_SPEED,
                AttributeModifier(NamespacedKey(KitPvp.INSTANCE, "magic_wand"), -2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND)
            )
        }
    }

    @EventHandler
    fun onMagicAttack(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return

        if (ExtendedPlayer.from(player).inSpawn()) return

        if (item.isThisCustomItem() && event.action.isLeftClick) {
            if (player.attackCooldown == 1f) {
                val location = player.eyeLocation.subtract(0.0, 0.25, 0.0)
                for (i in 1..40) {
                    player.world.spawnParticle(
                        Particle.DUST,
                        location.clone().add(location.direction.clone().multiply(i * 0.2)),
                        1,
                        0.0,
                        0.0,
                        0.0,
                        Particle.DustOptions(if (i % 2 == 0) Color.PURPLE else Color.RED, 1f)
                    )
                }
                val target = player.getTargetEntity(10)
                if (target is LivingEntity) {
                    target.damage(4.0, DamageSource.builder(DamageType.MAGIC).withDirectEntity(player).withCausingEntity(player).build())
                    player.playSound(player.location, Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 0f)
                }
            }
        }
    }
}
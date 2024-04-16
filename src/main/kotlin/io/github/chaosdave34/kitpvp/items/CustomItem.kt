package io.github.chaosdave34.kitpvp.items

import io.github.chaosdave34.ghutils.persistentdatatypes.UUIDPersistentDataType
import io.github.chaosdave34.ghutils.utils.PDCUtils
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.abilities.Ability
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import io.github.chaosdave34.kitpvp.utils.Describable
import io.github.chaosdave34.kitpvp.utils.ItemUtilities
import org.bukkit.entity.ThrowableProjectile
import org.bukkit.event.entity.ProjectileLaunchEvent
import java.util.*

abstract class CustomItem(val material: Material, val id: String, private val stackable: Boolean = true, private val preventPlacingAndUsing: Boolean = false) :
    Listener, ItemUtilities, Describable {
    abstract fun getName(): Component

    open fun getDescription() = emptyList<Component>()

    open fun getAbilities() = emptyList<Ability>()

    fun build(count: Int = 1): ItemStack {
        val itemStack = ItemStack(material, count)
        val itemMeta = itemStack.itemMeta

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        itemMeta.displayName(getName())

        PDCUtils.setId(itemMeta, id)

        if (!stackable) {
            val container = itemMeta.persistentDataContainer
            container.set(NamespacedKey(KitPvp.INSTANCE, "uuid"), UUIDPersistentDataType(), UUID.randomUUID())
        }

        itemStack.itemMeta = itemMeta

        getAbilities().forEach { ability -> ability.apply(itemStack) }

        additionalModifications(itemStack)

        setCustomLore(itemStack)

        return itemStack
    }

    private fun setCustomLore(itemStack: ItemStack) {
        // Description
        val lore: MutableList<Component> = getDescription().toMutableList()

        if (lore.isNotEmpty()) lore.add(Component.empty())

        // Enchantments
        val enchantmentLore: MutableList<Component> = mutableListOf()

        for ((enchantment, level) in itemStack.enchantments) {
            val component = enchantment.displayName(level).color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false)
            enchantmentLore.add(component)
        }

        enchantmentLore.sortWith(Comparator.comparing { PlainTextComponentSerializer.plainText().serialize(it).lowercase() })

        lore += enchantmentLore

        if (enchantmentLore.isNotEmpty()) lore.add(Component.empty())

        // Abilities
        val abilities = KitPvp.INSTANCE.abilityHandler.getItemAbilities(itemStack).iterator()
        while (abilities.hasNext()) {
            val ability = abilities.next()

            val name: Component = Component.text("Ability: ", NamedTextColor.GREEN)
                .append(Component.text(ability.name, NamedTextColor.GOLD))
                .append(Component.text("  "))
                .append(Component.text(ability.type.displayName, NamedTextColor.YELLOW, TextDecoration.BOLD))
                .decoration(TextDecoration.ITALIC, false)

            lore.add(name)

            lore.addAll(ability.getDescription())

            val cooldown: Component = Component.text("Cooldown: ", NamedTextColor.DARK_GRAY)
                .append(Component.text(ability.cooldown, NamedTextColor.GREEN))
                .append(Component.text("s", NamedTextColor.GREEN))
                .decoration(TextDecoration.ITALIC, false)

            lore.add(cooldown)

            if (abilities.hasNext()) lore.add(Component.empty())
        }

        if (lore.isNotEmpty() && PlainTextComponentSerializer.plainText().serialize(lore[lore.size - 1]).isEmpty()) {
            lore.removeAt(lore.size - 1)
        }

        itemStack.lore(lore)
    }

    protected open fun additionalModifications(itemStack: ItemStack) {}

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (preventPlacingAndUsing && id == CustomItemHandler.getCustomItemId(event.itemInHand)) event.isCancelled = true
    }

    @EventHandler
    fun onUse(event: PlayerInteractEvent) {
        val item = event.item ?: return

        if (preventPlacingAndUsing && id == CustomItemHandler.getCustomItemId(item)) {
            if (event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_BLOCK) event.isCancelled = true
        }
    }

    @EventHandler
    fun onLaunch(event: ProjectileLaunchEvent) {
        val throwableProjectile = event.entity
        if (throwableProjectile is ThrowableProjectile && throwableProjectile.item.isThisCustomItem()) {
            if (preventPlacingAndUsing) event.isCancelled = true
        }
    }

    fun ItemStack.isThisCustomItem() = id == CustomItemHandler.getCustomItemId(this)

    protected fun createSimpleItemName(name: String): Component {
        return Component.text(name).decoration(TextDecoration.ITALIC, false)
    }
}
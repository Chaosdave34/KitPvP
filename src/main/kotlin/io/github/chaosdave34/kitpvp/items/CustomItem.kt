package io.github.chaosdave34.kitpvp.items

import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.items.CustomItemHandler.Companion.getCustomItemId
import io.github.chaosdave34.kitpvp.utils.Describable
import io.github.chaosdave34.kitpvp.utils.ItemUtilities
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.ThrowableProjectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

open class CustomItem(
    val material: Material,
    val id: String,
    val name: String,
    private val stackable: Boolean = true,
    private val preventPlacingAndUsing: Boolean = false
) :
    Listener, ItemUtilities, Describable {

    fun getName(): Component {
        return createSimpleItemName(name)
    }

    open fun getDescription() = emptyList<Component>()

    fun build(count: Int = 1): ItemStack {
        val itemStack = ItemStack.of(material, count)
        val itemMeta = itemStack.itemMeta

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        itemMeta.displayName(getName())

        if (!stackable) itemMeta.setMaxStackSize(1)

        val container = itemMeta.persistentDataContainer

        container.set(NamespacedKey(KitPvp.INSTANCE, "id"), PersistentDataType.STRING, id)

        val enchantmentContainer = container.adapterContext.newPersistentDataContainer()
        container.set(NamespacedKey(KitPvp.INSTANCE, "enchantments"), PersistentDataType.TAG_CONTAINER, enchantmentContainer)

        itemStack.itemMeta = itemMeta

        additionalModifications(itemStack)

        setCustomLore(itemStack)

        return itemStack
    }

    private fun setCustomLore(itemStack: ItemStack) {
        // Description
        val lore: MutableList<Component> = getDescription().toMutableList()

        if (lore.isNotEmpty()) lore.add(Component.empty())

        // Enchantments
//        val enchantmentLore: MutableList<Component> = mutableListOf()
//
//        for ((enchantment, level) in itemStack.enchantments) {
//            val component = enchantment.displayName(level).color(NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false)
//            enchantmentLore.add(component)
//        }
//
//        enchantmentLore.sortWith(Comparator.comparing { PlainTextComponentSerializer.plainText().serialize(it).lowercase() })
//
//        lore += enchantmentLore
//
//        if (enchantmentLore.isNotEmpty()) lore.add(Component.empty())

        if (lore.isNotEmpty() && PlainTextComponentSerializer.plainText().serialize(lore[lore.size - 1]).isEmpty()) {
            lore.removeAt(lore.size - 1)
        }

        itemStack.lore(lore)
    }

    protected open fun additionalModifications(itemStack: ItemStack) {}

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (preventPlacingAndUsing && id == event.itemInHand.getCustomItemId()) event.isCancelled = true
    }

    @EventHandler
    fun onUse(event: PlayerInteractEvent) {
        val item = event.item ?: return

        if (preventPlacingAndUsing && id == item.getCustomItemId()) {
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

    private fun createSimpleItemName(name: String): Component {
        return Component.text(name).decoration(TextDecoration.ITALIC, false)
    }

    fun ItemStack.isThisCustomItem() = id == this.getCustomItemId()
}
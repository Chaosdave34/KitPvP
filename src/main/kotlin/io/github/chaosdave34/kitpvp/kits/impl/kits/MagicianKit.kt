package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class MagicianKit : Kit("magician", "Magician") {

    override fun getHeadContent(): ItemStack {
        val leatherHelmet = ItemStack(Material.LEATHER_HELMET)
        leatherHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherHelmet.setLeatherArmorColor(Color.RED)
        return leatherHelmet
    }

    override fun getChestContent(): ItemStack {
        val leatherChestplate = ItemStack(Material.LEATHER_CHESTPLATE)
        leatherChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherChestplate.setLeatherArmorColor(Color.NAVY)
        return leatherChestplate
    }

    override fun getLegsContent(): ItemStack {
        val leatherLeggings = ItemStack(Material.LEATHER_LEGGINGS)
        leatherLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherLeggings.setLeatherArmorColor(Color.NAVY)
        return leatherLeggings
    }

    override fun getFeetContent(): ItemStack {
        val leatherBoots = ItemStack(Material.LEATHER_BOOTS)
        leatherBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        leatherBoots.setLeatherArmorColor(Color.BLACK)
        return leatherBoots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.MAGIC_WAND.build(),
            ItemStack(Material.BOOK),
            ItemStack(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        val splashPotion = ItemStack(Material.SPLASH_POTION, 2)
        splashPotion.editMeta(PotionMeta::class.java) { potionMeta: PotionMeta -> potionMeta.basePotionType = PotionType.INSTANT_HEAL }

        return arrayOf(
            splashPotion,
            ItemStack(Material.GILDED_BLACKSTONE, 32),
        )
    }

    @EventHandler
    fun onSlotChange(event: PlayerItemHeldEvent) {
        val player = event.player
        if (player.isKitActive()) {
            val slot: ItemStack? = player.inventory.getItem(event.newSlot)

            val inventory = player.inventory

            if (slot?.type != Material.BOOK) {
                player.removePotionEffect(PotionEffectType.SPEED)
                player.removePotionEffect(PotionEffectType.INVISIBILITY)
                inventory.helmet = getHeadContent()
                inventory.chestplate = getChestContent()
                inventory.leggings = getLegsContent()
                inventory.boots = getFeetContent()
            } else {
                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, -1, 3, false, false, false))
                player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, -1, 0, false, false, false))
                inventory.helmet = ItemStack(Material.AIR)
                inventory.chestplate = ItemStack(Material.AIR)
                inventory.leggings = ItemStack(Material.AIR)
                inventory.boots = ItemStack(Material.AIR)
            }
        }
    }
}
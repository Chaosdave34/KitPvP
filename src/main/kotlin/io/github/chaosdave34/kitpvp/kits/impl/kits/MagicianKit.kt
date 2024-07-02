package io.github.chaosdave34.kitpvp.kits.impl.kits

import io.github.chaosdave34.kitpvp.items.CustomItemHandler
import io.github.chaosdave34.kitpvp.kits.Kit
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import io.github.chaosdave34.kitpvp.ultimates.UltimateHandler
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
        val helmet = ItemStack.of(Material.LEATHER_HELMET)
        helmet.addEnchantment(Enchantment.PROTECTION, 2)
        helmet.setLeatherArmorColor(Color.RED)
        return helmet
    }

    override fun getChestContent(): ItemStack {
        val chestplate = ItemStack.of(Material.LEATHER_CHESTPLATE)
        chestplate.addEnchantment(Enchantment.PROTECTION, 2)
        chestplate.setLeatherArmorColor(Color.NAVY)
        return chestplate
    }

    override fun getLegsContent(): ItemStack {
        val leggings = ItemStack.of(Material.LEATHER_LEGGINGS)
        leggings.addEnchantment(Enchantment.PROTECTION, 2)
        leggings.setLeatherArmorColor(Color.NAVY)
        return leggings
    }

    override fun getFeetContent(): ItemStack {
        val boots = ItemStack.of(Material.LEATHER_BOOTS)
        boots.addEnchantment(Enchantment.PROTECTION, 2)
        boots.setLeatherArmorColor(Color.BLACK)
        return boots
    }

    override fun getInventoryContent(): Array<ItemStack?> {
        return arrayOf(
            CustomItemHandler.MAGIC_WAND.build(),
            ItemStack.of(Material.BOOK),
            ItemStack.of(Material.WATER_BUCKET),
        )
    }

    override fun getKillRewards(): Array<ItemStack> {
        val splashPotion = ItemStack.of(Material.SPLASH_POTION, 2)
        splashPotion.editMeta(PotionMeta::class.java) { potionMeta: PotionMeta -> potionMeta.basePotionType = PotionType.HEALING }

        return arrayOf(
            splashPotion,
            ItemStack.of(Material.GILDED_BLACKSTONE, 32),
        )
    }

    override fun getUltimate(): Ultimate = UltimateHandler.LEVITATE

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
                inventory.helmet = ItemStack.of(Material.AIR)
                inventory.chestplate = ItemStack.of(Material.AIR)
                inventory.leggings = ItemStack.of(Material.AIR)
                inventory.boots = ItemStack.of(Material.AIR)
            }
        }
    }
}
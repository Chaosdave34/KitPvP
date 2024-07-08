package io.github.chaosdave34.kitpvp.guis

import com.google.common.io.ByteStreams
import io.github.chaosdave34.kitpvp.ExtendedPlayer
import io.github.chaosdave34.kitpvp.KitPvp
import io.github.chaosdave34.kitpvp.elytrakits.ElytraKit
import io.github.chaosdave34.kitpvp.elytrakits.ElytraKitHandler
import io.github.chaosdave34.kitpvp.guis.GuiHandler.Companion.hideAttributes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil
import kotlin.math.max

object Guis {
    val SERVER_SELECTOR = Gui("default")
    val TRAINER = Gui("overview")
    val ELYTRA_KITS = Gui("default")
    val COSMETICS = Gui("overview")
    val INVENTORY = Gui("overview")

    fun create() {
        SERVER_SELECTOR.createDefaultPage(Component.text("Server Selector"), 4) { page, player ->

            val amusementParkButton = ItemStack.of(Material.FIREWORK_ROCKET)
            amusementParkButton.editMeta {
                it.displayName(Component.text("Freizeitpark", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                it.lore(listOf(Component.text("(whitelisted)", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)))
                it.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
            }

            page.createButton(11, amusementParkButton) {
                val message = ByteStreams.newDataOutput()
                message.writeUTF("Connect")
                message.writeUTF("amusementpark")
                (player).sendPluginMessage(KitPvp.INSTANCE, "BungeeCord", message.toByteArray())
                player.closeInventory()
            }

            val survivalButton = ItemStack.of(Material.IRON_SWORD)
            survivalButton.editMeta {
                it.displayName(Component.text("Survival", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                it.lore(listOf(Component.text("(whitelisted)", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)))
                it.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                it.hideAttributes()
            }

            page.createButton(15, survivalButton) {
                val message = ByteStreams.newDataOutput()
                message.writeUTF("Connect")
                message.writeUTF("survival")
                (player).sendPluginMessage(KitPvp.INSTANCE, "BungeeCord", message.toByteArray())
                player.closeInventory()
            }

            page.createCloseButton(31)
            page.fillEmpty()
        }

        TRAINER.createDefaultPage(Component.text("Trainer"), 4) { page, player ->
            val extendedPlayer = ExtendedPlayer.from(player)

            val selectedAbility1 = KitPvp.INSTANCE.abilityHandler.abilities[extendedPlayer.selectedSetup.abilities[0]]?.name ?: "None"
            val selectedAbility2 = KitPvp.INSTANCE.abilityHandler.abilities[extendedPlayer.selectedSetup.abilities[1]]?.name ?: "None"

            val abilityButton = ItemStack.of(Material.BREEZE_ROD)
            abilityButton.editMeta {
                it.displayName(Component.text("Abilities", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                it.lore(
                    listOf(
                        Component.text("Selected:", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text(selectedAbility1, NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false),
                        Component.text(selectedAbility2, NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)
                    )
                )
            }

            page.createButton(11, abilityButton) { event ->
                TRAINER.openPage("abilities", player)
            }

            val selectedUltimate = KitPvp.INSTANCE.ultimateHandler.ultimates[extendedPlayer.selectedSetup.ultimate]?.name ?: "None"

            val ultimateButton = ItemStack.of(Material.BLAZE_ROD)
            ultimateButton.editMeta {
                it.displayName(Component.text("Ultimates", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                it.lore(
                    listOf(
                        Component.text("Selected: ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text(selectedUltimate, NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
                    )
                )
            }

            page.createButton(15, ultimateButton) { event ->
                TRAINER.openPage("ultimates", player)
            }

            page.createCloseButton(31)
            page.fillEmpty()
        }

        TRAINER.createPage("abilities", Component.text("Abilities"), 6) { page, player ->
            val extendedPlayer = ExtendedPlayer.from(player)

            for ((i, ability) in KitPvp.INSTANCE.abilityHandler.abilities.values.sortedBy { it.name }.withIndex()) {
                val item = ability.getItem()

                if (ability.id in extendedPlayer.selectedSetup.abilities) {
                    item.editMeta {
                        it.setEnchantmentGlintOverride(true)
                        it.displayName(it.displayName()?.color(NamedTextColor.GREEN))
                    }
                }

                page.createButton(i, item) { event ->
                    val eventExtendedPlayer = ExtendedPlayer.from(player)
                    eventExtendedPlayer.selectedSetup.addAbility(ability)
                    TRAINER.openPage("abilities", player) // Todo: Improve updating inv
                }

                page.createButton(48, Material.ARROW, Component.text("Back").decoration(TextDecoration.ITALIC, false)) {
                    TRAINER.open(it.whoClicked as Player)
                }
                page.createCloseButton(49)
                page.fillEmpty()
            }
        }

        TRAINER.createPage("ultimates", Component.text("Ultimates"), 6) { page, player ->
            val extendedPlayer = ExtendedPlayer.from(player)

            for ((i, ultimate) in KitPvp.INSTANCE.ultimateHandler.ultimates.values.sortedBy { it.name }.withIndex()) {
                val item = ultimate.getItem()

                if (ultimate.id == extendedPlayer.selectedSetup.ultimate) {
                    item.editMeta {
                        it.setEnchantmentGlintOverride(true)
                        it.displayName(it.displayName()?.color(NamedTextColor.GREEN))
                    }
                }

                page.createButton(i, item) { event ->
                    val eventExtendedPlayer = ExtendedPlayer.from(player)
                    eventExtendedPlayer.selectedSetup.setUltimate(ultimate)
                    TRAINER.openPage("ultimates", player) // Todo: Improve updating inv
                }

                page.createButton(48, Material.ARROW, Component.text("Back").decoration(TextDecoration.ITALIC, false)) {
                    TRAINER.open(it.whoClicked as Player)
                }
                page.createCloseButton(49)
                page.fillEmpty()
            }
        }

        ELYTRA_KITS.createDefaultPage(Component.text("Kits"), 5) { page, player ->
            val setIcon: (slot: Int, kit: ElytraKit) -> Unit = { slot, kit ->
                val name = Component.text(kit.name, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)

                val item = ItemStack.of(kit.icon)
                item.editMeta {
                    it.displayName(name)
                    it.setEnchantmentGlintOverride(kit.id == ExtendedPlayer.from(player).selectedElytraKitId)
                    it.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                    it.hideAttributes()
                }

                page.createButton(slot, item) {
                    val eventPlayer = it.whoClicked as Player
                    val extendedPlayer = ExtendedPlayer.from(eventPlayer)

                    if (extendedPlayer.selectedElytraKitId != kit.id) {
                        kit.apply(eventPlayer)
                        eventPlayer.sendMessage(
                            Component.text("You have selected the Kit ", NamedTextColor.GRAY)
                                .append(Component.text(kit.name, NamedTextColor.GREEN))
                                .append(Component.text(".", NamedTextColor.GRAY))
                        )
                    }
                    eventPlayer.inventory.close()
                }

            }

            setIcon(11, ElytraKitHandler.KNIGHT)
            setIcon(12, ElytraKitHandler.SNIPER)
            setIcon(13, ElytraKitHandler.PYRO)
            setIcon(14, ElytraKitHandler.TANK)
            setIcon(15, ElytraKitHandler.KNOCKER)
            setIcon(20, ElytraKitHandler.ROCKET_LAUNCHER)
            setIcon(21, ElytraKitHandler.POSEIDON)
            setIcon(22, ElytraKitHandler.TELEPORTER)
            setIcon(23, ElytraKitHandler.HEALER)
            setIcon(24, ElytraKitHandler.CHEMIST)

            page.createCloseButton(40)

            page.fillEmpty()
        }

        COSMETICS.createDefaultPage(Component.text("Cosmetics"), 3) { page, player ->
            val extendedPlayer: ExtendedPlayer = ExtendedPlayer.from(player)

            val selectedProjectileTrail = KitPvp.INSTANCE.cosmeticHandler.projectileTrails[extendedPlayer.projectileTrailId]?.name ?: "None"

            val projectileTrailButton = ItemStack.of(Material.ARROW)
            projectileTrailButton.editMeta {
                it.displayName(Component.text("Projectile Trails").decoration(TextDecoration.ITALIC, false))
                it.lore(listOf(Component.text("Selected: $selectedProjectileTrail", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)))
                it.hideAttributes()
            }

            page.createButton(11, projectileTrailButton) {
                COSMETICS.openPage("projectile_trails", it.whoClicked as Player)
            }

            val selectedKillEffect = KitPvp.INSTANCE.cosmeticHandler.killEffects[extendedPlayer.killEffectId]?.name ?: "None"

            val killEffectButton = ItemStack.of(Material.IRON_SWORD)
            killEffectButton.editMeta {
                it.displayName(Component.text("Kill Effects").decoration(TextDecoration.ITALIC, false))
                it.lore(listOf(Component.text("Selected: $selectedKillEffect", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)))
                it.hideAttributes()
            }

            page.createButton(15, killEffectButton) {
                COSMETICS.openPage("kill_effects", it.whoClicked as Player)
            }
            page.createCloseButton(22)

            page.fillEmpty()
        }

        val killEffects = KitPvp.INSTANCE.cosmeticHandler.killEffects.values.toList()
        val killEffectsRows = max(ceil((killEffects.size + 1) / 9.0), 1.0) + 1

        COSMETICS.createPage("kill_effects", Component.text("Kill Effects"), killEffectsRows.toInt()) { page, player ->
            val extendedPlayer = ExtendedPlayer.from(player)

            // Todo: add glint and color if selected
            page.createButton(0, Material.BARRIER, Component.text("None", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)) {
                ExtendedPlayer.from(it.whoClicked as Player).killEffectId = null
                it.whoClicked.closeInventory()
            }

            var j = 1
            for (killEffect in killEffects) {
                var name = Component.text(killEffect.name, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)

                var glint = false
                if (extendedPlayer.killEffectId == killEffect.id) {
                    name = name.color(NamedTextColor.GREEN)
                    glint = true
                }

                val lore: MutableList<Component> = mutableListOf()
                if (extendedPlayer.getLevel() < killEffect.levelRequirement) {
                    name = name.color(NamedTextColor.RED)
                    lore.add(
                        Component.text("Unlocked at level " + killEffect.levelRequirement + ".", NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
                    )
                }

                val item = ItemStack.of(killEffect.icon)
                item.editMeta {
                    it.displayName(name)
                    it.setEnchantmentGlintOverride(glint)
                    it.lore(lore)
                }

                page.createButton(j, item) {
                    val eventExtendedPlayer = ExtendedPlayer.from(it.whoClicked as Player)
                    if (extendedPlayer.getLevel() >= killEffect.levelRequirement) {
                        eventExtendedPlayer.killEffectId = killEffect.id
                        it.whoClicked.closeInventory()
                    }
                }

                page.createButton(page.rows * 9 - 6, Material.ARROW, Component.text("Back").decoration(TextDecoration.ITALIC, false)) {
                    COSMETICS.open(it.whoClicked as Player)
                }
                page.createCloseButton(page.rows * 9 - 5)

                page.fillEmpty()

                j++
            }
        }

        val projectileTrails = KitPvp.INSTANCE.cosmeticHandler.projectileTrails.values.toList()
        val projectileTrailsRows = max(ceil((projectileTrails.size + 1) / 9.0), 1.0) + 1

        COSMETICS.createPage("projectile_trails", Component.text("Projectile Trails"), projectileTrailsRows.toInt()) { page, player ->
            val extendedPlayer = ExtendedPlayer.from(player)

            // Todo: add glint and color if selected
            page.createButton(0, Material.BARRIER, Component.text("None", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)) {
                ExtendedPlayer.from(it.whoClicked as Player).projectileTrailId = null
                it.whoClicked.closeInventory()
            }

            var j = 1
            for (projectileTrail in projectileTrails) {
                var name = Component.text(projectileTrail.name, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false)

                var glint = false
                if (extendedPlayer.projectileTrailId == projectileTrail.id) {
                    name = name.color(NamedTextColor.GREEN)
                    glint = true
                }

                val lore: MutableList<Component> = mutableListOf()
                if (extendedPlayer.getLevel() < projectileTrail.levelRequirement) {
                    name = name.color(NamedTextColor.RED)
                    lore.add(
                        Component.text("Unlocked at level " + projectileTrail.levelRequirement + ".", NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
                    )
                }

                val item = ItemStack.of(projectileTrail.icon)
                item.editMeta {
                    it.displayName(name)
                    it.setEnchantmentGlintOverride(glint)
                    it.lore(lore)
                }

                page.createButton(j, item) {
                    val eventExtendedPlayer = ExtendedPlayer.from(it.whoClicked as Player)
                    if (extendedPlayer.getLevel() >= projectileTrail.levelRequirement) {
                        eventExtendedPlayer.projectileTrailId = projectileTrail.id
                        it.whoClicked.closeInventory()
                    }
                }

                page.createCloseButton(page.rows * 9 - 5)
                page.createButton(page.rows * 9 - 6, Material.ARROW, Component.text("Back").decoration(TextDecoration.ITALIC, false)) {
                    COSMETICS.open(it.whoClicked as Player)
                }

                page.fillEmpty()

                j++
            }
        }

        INVENTORY.createDefaultPage(Component.text("Shop"), 4) { page, player ->
            val extendedPlayer = ExtendedPlayer.from(player)

            val selectedWeapon1 = KitPvp.INSTANCE.customItemHandler.customItems[extendedPlayer.selectedSetup.weapons[0]]?.name ?: "None"
            val selectedWeapon2 = KitPvp.INSTANCE.customItemHandler.customItems[extendedPlayer.selectedSetup.weapons[1]]?.name ?: "None"

            val weaponsButton = ItemStack.of(Material.IRON_SWORD)
            weaponsButton.editMeta {
                it.displayName(Component.text("Weapons", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                it.lore(
                    listOf(
                        Component.text("Selected:", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text(selectedWeapon1, NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false),
                        Component.text(selectedWeapon2, NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
                    )
                )
                it.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                it.hideAttributes()
            }
            page.createButton(10, weaponsButton) { INVENTORY.openPage("weapons", player) }

            val armorButton = ItemStack.of(Material.IRON_CHESTPLATE)
            armorButton.editMeta {
                it.displayName(Component.text("Armor", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                it.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                it.hideAttributes()
            }
            page.createButton(12, armorButton) { INVENTORY.openPage("armor", player) }

            val utilityButton = ItemStack.of(Material.TOTEM_OF_UNDYING)
            utilityButton.editMeta {
                it.displayName(Component.text("Utility", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            }
            page.createButton(14, utilityButton) { INVENTORY.openPage("utility", player) }

            val potionButton = ItemStack.of(Material.POTION)
            potionButton.editMeta {
                it.displayName(Component.text("Potions", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
                it.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
            }
            page.createButton(16, potionButton) { INVENTORY.openPage("potions", player) }

            page.createCloseButton(31)
            page.fillEmpty()
        }

        INVENTORY.createPage("weapons", Component.text("Weapongs"), 6) { page, player ->
            val extendedPlayer = ExtendedPlayer.from(player)

            for ((i, weapon) in KitPvp.INSTANCE.customItemHandler.customItems.values
                .filter { it.material.equipmentSlot == EquipmentSlot.HAND }.sortedBy { it.name }.withIndex()) {
                val item = weapon.build()

                item.editMeta {
                    if (weapon.id in extendedPlayer.selectedSetup.weapons) {
                        it.setEnchantmentGlintOverride(true)
                        it.displayName(it.displayName()?.color(NamedTextColor.GREEN))
                    } else {
                        it.setEnchantmentGlintOverride(false)
                        it.displayName(it.displayName()?.color(NamedTextColor.WHITE))
                    }
                }

                page.createButton(i, item) {
                    val eventExtendedPlayer = ExtendedPlayer.from(player)
                    eventExtendedPlayer.selectedSetup.addWeapon(weapon)
                    INVENTORY.openPage("weapons", player) // Todo: Improve updating inv
                }
            }

            page.createButton(48, Material.ARROW, Component.text("Back").decoration(TextDecoration.ITALIC, false)) { INVENTORY.open(player) }
            page.createCloseButton(49)
            page.fillEmpty()
        }
    }
}
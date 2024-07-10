package io.github.chaosdave34.kitpvp

import io.github.chaosdave34.kitpvp.abilities.Ability
import io.github.chaosdave34.kitpvp.consumables.Consumable
import io.github.chaosdave34.kitpvp.customevents.CustomEventHandler
import io.github.chaosdave34.kitpvp.elytrakits.ElytraKit
import io.github.chaosdave34.kitpvp.elytrakits.ElytraKitHandler
import io.github.chaosdave34.kitpvp.events.PlayerSpawnEvent
import io.github.chaosdave34.kitpvp.items.CustomItem
import io.github.chaosdave34.kitpvp.pasives.Passive
import io.github.chaosdave34.kitpvp.textdisplays.TextDisplays
import io.github.chaosdave34.kitpvp.ultimates.Ultimate
import io.github.chaosdave34.kitpvp.utils.MathUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.reflect.KClass

class ExtendedPlayer(val uuid: UUID) {
    lateinit var selectedSetup: SelectedSetup

    var selectedElytraKitId: String
        private set
    var currentGame: GameType

    private var experiencePoints: Int
    var coins: Int
        private set
    private var bounty: Int

    private var highestKillStreaks: MutableMap<GameType, Int>
    private var totalKills: MutableMap<GameType, Int>
    private var totalDeaths: MutableMap<GameType, Int>

    var projectileTrailId: String? = null
    var killEffectId: String? = null

    var dailyChallenges: List<String>

    @Transient
    lateinit var attributes: Attributes

    @Transient
    var gameState: GameState = GameState.KITS_SPAWN
        set(gameState) {
            field = gameState
            updateScoreboardLines()
        }

    @Transient
    lateinit var scoreboard: Scoreboard

    @Transient
    var combatCooldown = 0

    @Transient
    var killStreak = 0

    @Transient
    private var morph: Entity? = null

    @Transient
    private var companion: Mob? = null

    // Set default values
    init {
        selectedElytraKitId = ElytraKitHandler.KNIGHT.id
        currentGame = GameType.KITS

        experiencePoints = 0
        coins = 0
        bounty = 0

        highestKillStreaks = mutableMapOf()
        totalKills = mutableMapOf()
        totalDeaths = mutableMapOf()

        createScoreboard()

        gameState = GameState.KITS_SPAWN

        dailyChallenges = mutableListOf()
    }

    fun checkAndMigrateSaveData() {
        if (!this::selectedSetup.isInitialized) selectedSetup = SelectedSetup(this)
        if (selectedSetup.extendedPlayer == null) selectedSetup.extendedPlayer = this

        if (!this::attributes.isInitialized) attributes = Attributes(this)
        if (attributes.extendedPlayer == null) attributes.extendedPlayer = this
    }

    companion object {
        @JvmStatic
        fun from(player: Player): ExtendedPlayer {
            return from(player.uniqueId)
        }

        @JvmStatic
        fun from(uuid: UUID): ExtendedPlayer {
            return KitPvp.INSTANCE.getExtendedPlayer(uuid)
        }
    }

    fun getPlayer() = Bukkit.getPlayer(uuid)

    fun inSpawn() = gameState == GameState.KITS_SPAWN || gameState == GameState.ELYTRA_SPAWN

    fun inGame() = gameState == GameState.KITS_IN_GAME || gameState == GameState.ELYTRA_IN_GAME

    private fun getSelectedElytraKit(): ElytraKit {
        val kit = KitPvp.INSTANCE.elytraKitHandler.kits[selectedElytraKitId]

        if (kit == null) {
            setSelectedElytraKitId(ElytraKitHandler.KNIGHT.id)
            return ElytraKitHandler.KNIGHT
        }
        return kit
    }

    fun setSelectedElytraKitId(kitId: String) {
        selectedElytraKitId = kitId
        updateScoreboardLines()
    }

    fun spawn() {
        spawn(currentGame)
    }

    fun spawn(gameType: GameType) {
        val player = getPlayer() ?: return

        if (selectedSetup.extendedPlayer == null) selectedSetup.extendedPlayer = this

        player.gameMode = GameMode.ADVENTURE
        currentGame = gameType

        unMorph()
        removeCompanion()

        player.foodLevel = 20
        player.saturation = 5f
        player.exhaustion = 0f

        player.fireTicks = 0
        player.freezeTicks = 0

        player.isGlowing = false

        player.setItemOnCursor(null)
        player.clearActiveItem()
        player.closeInventory(InventoryCloseEvent.Reason.DEATH)

        killStreak = 0
        combatCooldown = 0

        player.world.getEntitiesByClass(Trident::class.java).forEach { trident ->
            if (trident.shooter is Player && trident.shooter == player)
                trident.remove()
        }

        player.world.getEntitiesByClass(EnderPearl::class.java).forEach { pearl ->
            if (pearl.shooter is Player && pearl.shooter == player)
                pearl.remove()
        }

        if (!this::scoreboard.isInitialized) {
            createScoreboard()
        }

        when (currentGame) {
            GameType.KITS -> {
                player.teleport(Location(Bukkit.getWorld("world"), 2.0, 120.0, 10.0, 180f, 0f))
                gameState = GameState.KITS_SPAWN
                player.inventory.clear()
                selectedSetup.apply()

                object : BukkitRunnable() {
                    override fun run() {
                        if (gameType != GameType.KITS && getPlayer() == null) this.cancel()

                        // Action Bar
                        getPlayer()?.sendActionBar(createActionBarMessage())

                        // Mana Regen
                        if (attributes.mana < attributes.getMaxMana()) {
                            var manaRegen = attributes.getManaRegen()
                            if (gameState == GameState.KITS_SPAWN) manaRegen *= 5
                            attributes.mana += attributes.getMaxMana() * manaRegen

                            if (attributes.mana > attributes.getMaxMana()) attributes.mana = attributes.getMaxMana()
                        }
                    }

                }.runTaskTimer(KitPvp.INSTANCE, 0, 20)
            }

            GameType.ELYTRA -> {
                player.teleport(Location(Bukkit.getWorld("world_elytra"), 16.0, 200.0, 0.0, 90f, 0f))
                gameState = GameState.ELYTRA_SPAWN
                getSelectedElytraKit().apply(player)
            }
        }

        PlayerSpawnEvent(player).callEvent()

        updateScoreboardLines()
        updateDisplayName()
    }

    fun updateDisplayName() {
        val player = getPlayer() ?: return

        val icon = when (currentGame) {
            GameType.KITS -> "⚔"
            GameType.ELYTRA -> "\uD83D\uDEE1"
        }

        var name = Component.text("$icon [${getLevel()}] ${player.name}")

        if (bounty > 0) name = name.append { Component.text(" $bounty", NamedTextColor.GOLD) }

        player.playerListName(name)
    }

    private fun createScoreboard() {
        scoreboard = Bukkit.getScoreboardManager().newScoreboard
        val objective = scoreboard.registerNewObjective("default", Criteria.DUMMY, Component.text("KitPvP", NamedTextColor.YELLOW, TextDecoration.BOLD))
        objective.displaySlot = DisplaySlot.SIDEBAR
        getPlayer()?.scoreboard = scoreboard
    }

    fun updateScoreboardLines() { // Todo: Rework scoreboard for kits game mode
        val objective = scoreboard.getObjective("default") ?: return

        scoreboard.entries.forEach { entry -> scoreboard.resetScores(entry) }

        objective.getScore("Level: " + getLevel()).score = 7
        objective.getScore("Missing XP: §b" + getMissingExperience()).score = 6
        objective.getScore("Coins: §6$coins").score = 5
        objective.getScore(" ").score = 4

        val kitName = when (currentGame) {
            GameType.KITS -> "!PLACEHOLDER!"
            GameType.ELYTRA -> getSelectedElytraKit().name
        }
        objective.getScore("Kit: $kitName").score = 3

        objective.getScore("Kill Streak: $killStreak").score = 2
        objective.getScore("  ").score = 1
        objective.getScore("Status: " + (if (combatCooldown > 0) "§cFighting" else gameState.displayName)).score = 0
    }

    fun createActionBarMessage(): Component {
        val player = getPlayer() ?: return Component.text("")

        var health = player.health
        health = round(health * 10) / 10
        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0

        val mana = attributes.mana
        val maxMana = attributes.getMaxMana()

        var damageStacks = attributes.damageStack
        damageStacks = round(damageStacks * 10) / 10
        val enoughDamageStacks = damageStacks >= (selectedSetup.getUltimate()?.damageStackCost ?: 0.0)

        val message = listOf(
            Component.text("$health/$maxHealth ❤", NamedTextColor.RED),
            Component.text("$mana/$maxMana ʬ", NamedTextColor.DARK_AQUA),
            Component.text("$damageStacks ☄", NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, enoughDamageStacks)
        )

        return Component.join(JoinConfiguration.separator(Component.text("   ")), message)
    }

    fun updatePlayerListFooter() {
        val player = getPlayer() ?: return

//        var footer = Component.newline().append(Component.text("Daily Challenges:", NamedTextColor.GREEN))

//        dailyChallenges.forEach { challengeId ->
//            val challenge = KitPvp.INSTANCE.challengesHandler.getChallenge(challengeId) ?: return@forEach
//            val textColor = if (challenge.getProgress(player) == challenge.amount) NamedTextColor.GREEN else NamedTextColor.WHITE
//            footer = footer.append(Component.newline()).append(Component.text("${challenge.name} ${challenge.progress[player]}/${challenge.amount}", textColor))
//        }

        val footer = Component.newline()
            .append(Component.text("============================", NamedTextColor.YELLOW, TextDecoration.BOLD))

        player.sendPlayerListFooter(footer)
    }

    fun updateDailyChallenges() {
//        dailyChallenges = KitPvp.INSTANCE.challengesHandler.threeRandomChallenges.map { it.id }
//        Bukkit.getScheduler().runTaskLater(KitPvp.INSTANCE, Consumer { getPlayer()?.sendMessage(Component.text("You have new daily challenges!")) }, 1)
//
//        KitPvp.INSTANCE.challengesHandler.resetProgress(getPlayer())
    }

    private fun updatePersonalStatisticsDisplay(gameType: GameType) {
        when (gameType) {
            GameType.KITS -> getPlayer()?.let { TextDisplays.PERSONAL_STATISTICS_KITS.updateText(it) }
            GameType.ELYTRA -> getPlayer()?.let { TextDisplays.PERSONAL_STATISTICS_ELYTRA.updateText(it) }
        }
    }

    private fun incrementsKillStreak() {
        killStreak++

        if (killStreak % 5 == 0) {
            Bukkit.broadcast(Component.text("${getPlayer()?.name} has reached a kill streak of $killStreak"))
            receivedBounty(killStreak / 5 * 100)
        }

        if (killStreak > getHighestKillStreak(currentGame)) {
            highestKillStreaks[currentGame] = killStreak
            updatePersonalStatisticsDisplay(currentGame)
        }

        checkHighestKillStreakHighscore(currentGame)
        updateScoreboardLines()
    }

    fun getHighestKillStreak(gameType: GameType): Int {
        if (!highestKillStreaks.contains(gameType)) highestKillStreaks[gameType] = 0

        return highestKillStreaks[gameType] ?: 0
    }

    private fun checkHighestKillStreakHighscore(gameType: GameType) {
        val highestKillStreaks = KitPvp.INSTANCE.getHighestKillStreaks(gameType)
        if ((highestKillStreaks.size < 5 && !highestKillStreaks.containsKey(uuid)) || killStreak > highestKillStreaks.values.min()) {

            if (highestKillStreaks.size == 5 && !highestKillStreaks.containsKey(uuid)) {
                for (key in highestKillStreaks.keys) {
                    if (highestKillStreaks[key] == highestKillStreaks.values.min()) {
                        highestKillStreaks.remove(key)
                        break
                    }
                }
            }

            highestKillStreaks[uuid] = killStreak

            when (gameType) {
                GameType.KITS -> TextDisplays.HIGHEST_KILL_STREAKS_KITS.updateTextForAll()
                GameType.ELYTRA -> TextDisplays.HIGHEST_KILL_STREAKS_ELYTRA.updateTextForAll()
            }
        }
    }

    fun getTotalKills(gameType: GameType): Int {
        if (!totalKills.contains(gameType)) totalKills[gameType] = 0

        return totalKills[gameType] ?: 0
    }

    private fun incrementTotalKills() {
        totalKills[currentGame] = (totalKills[currentGame] ?: 0) + 1
        updatePersonalStatisticsDisplay(currentGame)
    }

    fun getTotalDeaths(gameType: GameType): Int {
        if (!totalDeaths.contains(gameType)) totalDeaths[gameType] = 0

        return totalDeaths[gameType] ?: 0
    }

    fun incrementTotalDeaths() {
        totalDeaths[currentGame] = (totalDeaths[currentGame] ?: 0) + 1
        updatePersonalStatisticsDisplay(currentGame)
    }

    fun addExperiencePoints(amount: Int) {
        val oldLevel = getLevel()

        experiencePoints += amount
        updateScoreboardLines()

        if (getLevel() > oldLevel) {
            updateDisplayName()
            getPlayer()?.sendMessage(Component.text("You have reached Level ${getLevel()}."))

            val highestLevels = KitPvp.INSTANCE.highestLevels

            if (highestLevels.size < 5 || getLevel() > highestLevels.values.min()) {
                if (highestLevels.size == 5 && !highestLevels.contains(uuid)) {
                    for (key in highestLevels.keys) {
                        if (highestLevels[key] == Collections.min(highestLevels.values)) {
                            highestLevels.remove(key)
                            break
                        }
                    }
                }

                highestLevels[uuid] = getLevel()

                TextDisplays.updateHighestLevels()
            }

        }
    }

    fun getLevel(): Int {
        var exp = experiencePoints
        var i = 1
        while (exp >= 20 + (i - 1) * 5) {
            exp -= 20 + (i - 1) * 5
            i++
        }

        return i
    }

    private fun getMissingExperience(): Int {
        return getRequiredExperienceUntilLevel(getLevel() + 1) - experiencePoints
    }

    private fun getRequiredExperienceUntilLevel(level: Int): Int {
        if (level == 1) return 0
        if (level == 2) return 20
        return (level - 1) * 20 + (MathUtils.binomialCoefficient(level - 1, 2) * 5)
    }

    fun addCoins(amount: Int) {
        coins += amount
        updateScoreboardLines()
    }

    fun purchase(amount: Int): Boolean {
        if (amount <= coins) {
            coins -= amount
            updateDisplayName()
            return true
        }
        return false
    }

    fun <T : LivingEntity> morph(entityType: KClass<T>) {
        val player = getPlayer() ?: return
        player.gameMode = GameMode.SPECTATOR

        morph = player.world.spawn(player.location, entityType.java) { entity ->
            entity.setMetadata("morph", FixedMetadataValue(KitPvp.INSTANCE, player.uniqueId))
            entity.addPassenger(player)
        }
    }

    fun unMorph() {
        val player = getPlayer() ?: return
        if (morph != null) {
            player.gameMode = GameMode.SURVIVAL
            morph?.removePassenger(player)
            morph?.remove()
            morph = null
        }
    }

    fun spawnCompanion() { // Todo: update after reworking companion
//        val player = getPlayer() ?: return
//
//        val comp = getSelectedKitsKit().getCompanion()
//        if (comp == null) {
//            if (companion != null) removeCompanion()
//            return
//        }
//
//        if (companion == null) {
//            companion = player.world.addEntity(comp.createCompanion(player))
//        }
    }

    private fun reviveCompanion() {
        if (companion != null) {
            removeCompanion()
            spawnCompanion()
        }
    }

    fun removeCompanion() {
        companion?.remove()
        companion = null

    }

    fun enterCombat() {
        if (combatCooldown == 0) {
            combatCooldown = 5
            updateScoreboardLines()

            object : BukkitRunnable() {
                override fun run() {
                    combatCooldown--
                    if (combatCooldown < 0) {
                        this.cancel()
                        updateScoreboardLines()
                        combatCooldown = 0
                    }
                }
            }.runTaskTimer(KitPvp.INSTANCE, 0, 20)

        }
        combatCooldown = 5
    }

    fun receivedBounty(amount: Int) {
        val message: Component = if (bounty == 0) {
            Component.text("A bounty of " + amount + " coins has been placed on " + getPlayer()?.name + ".")
        } else {
            Component.text("The bounty on " + getPlayer()?.name + " has been increased by " + amount + " coins to a total of " + (bounty + amount) + " coins.")
        }

        bounty += amount
        Bukkit.broadcast(message)
        updateDisplayName()
    }

    private fun claimBounty(claimer: Player) {
        Bukkit.broadcast(Component.text("The bounty on " + getPlayer()?.name + " has been claimed by " + claimer.name + "."))

        bounty = 0
        updateDisplayName()
    }

    fun killedPlayer(victim: Player) {
        val player = getPlayer() ?: return

        if (victim == getPlayer()) return

        incrementsKillStreak()
        incrementTotalKills()

        val extendedVictim = from(victim)
        var xpReward: Int = 10 + (extendedVictim.getLevel() * 0.25).toInt()
        if (CustomEventHandler.DOUBLE_COINS_AND_EXPERIENCE_EVENT.isActive) {
            xpReward *= 2
        }

        var coinReward: Int = (xpReward * 1.5).roundToInt()

        if (extendedVictim.bounty > 0) {
            coinReward += extendedVictim.bounty
            extendedVictim.claimBounty(player)
        }

        addExperiencePoints(xpReward)
        addCoins(coinReward)

        val info: Component = Component.text("Killed ${victim.name}: ")
            .append(Component.text("+${xpReward}XP ", NamedTextColor.AQUA))
            .append(Component.text("+$coinReward coins", NamedTextColor.GOLD))
        player.sendActionBar(info)

        when (currentGame) {
            //GameType.KITS -> player.inventory.addItem(*getSelectedKitsKit().getKillRewards())
            GameType.KITS -> {} // Todo: update after rework
            GameType.ELYTRA -> player.inventory.addItem(*getSelectedElytraKit().getKillRewards())
        }

        reviveCompanion()

        KitPvp.INSTANCE.cosmeticHandler.triggerKillEffect(player, victim)
    }


    enum class GameType {
        KITS,
        ELYTRA
    }

    enum class GameState(val displayName: String) {
        KITS_SPAWN("§aIdle"),
        ELYTRA_SPAWN("§aIdle"),
        KITS_IN_GAME("§eActive"),
        ELYTRA_IN_GAME("§eActive"),
        DEBUG("§0Debug")
    }

    class Attributes(@Transient var extendedPlayer: ExtendedPlayer?) {
        var mana = 0.0
        var damageStack = 0.0

        private val baseMaxMana = 100.0
        private val baseManaRegen = 0.1

        private val baseDefense = 10.0

        fun getMaxMana(): Double {
            return baseMaxMana
        }

        fun getManaRegen(): Double {
            return baseManaRegen
        }

        fun getDefense(): Double {
            return baseDefense
        }
    }

    class SelectedSetup(@Transient var extendedPlayer: ExtendedPlayer?) {
        var helmet: String? = null
        var chestplate: String? = null
        var leggings: String? = null
        var boots: String? = null

        val weapons: Array<String?> = arrayOfNulls(2)

        val consumables: Array<String?> = arrayOfNulls(2)

        var passive: String? = null

        val abilities: Array<String?> = arrayOfNulls(2)
        var ultimate: String? = null

        fun getHelmet() = KitPvp.INSTANCE.customItemHandler.customItems[helmet]

        fun setHelmet(helmet: CustomItem) {
            this.helmet = helmet.id
            extendedPlayer?.getPlayer()?.inventory?.helmet = helmet.build()
        }

        fun getChestplate() = KitPvp.INSTANCE.customItemHandler.customItems[chestplate]

        fun setChestplate(chestplate: CustomItem) {
            this.chestplate = chestplate.id
            extendedPlayer?.getPlayer()?.inventory?.chestplate = chestplate.build()
        }

        fun getLeggings() = KitPvp.INSTANCE.customItemHandler.customItems[leggings]

        fun setLeggins(leggings: CustomItem) {
            this.leggings = leggings.id
            extendedPlayer?.getPlayer()?.inventory?.leggings = leggings.build()
        }

        fun getBoots() = KitPvp.INSTANCE.customItemHandler.customItems[boots]

        fun setBoots(boots: CustomItem) {
            this.boots = boots.id
            extendedPlayer?.getPlayer()?.inventory?.boots = boots.build()
        }

        fun getWeapon1() = KitPvp.INSTANCE.customItemHandler.customItems[weapons[0]]
        fun getWeapon2() = KitPvp.INSTANCE.customItemHandler.customItems[weapons[1]]

        fun addWeapon(weapon: CustomItem) {
            if (weapon.id in weapons) return
            val inventory = extendedPlayer?.getPlayer()?.inventory ?: return

            if (weapons[0] == null) weapons[0] = weapon.id
            else if (weapons[1] == null) weapons[1] = weapon.id
            else {
                weapons[1] = weapons[0]
                weapons[0] = weapon.id
            }

            inventory.setItem(0, getWeapon1()?.build())
            inventory.setItem(1, getWeapon2()?.build())
        }

        fun getAbility1() = KitPvp.INSTANCE.abilityHandler.abilities[abilities[0]]
        fun getAbility2() = KitPvp.INSTANCE.abilityHandler.abilities[abilities[1]]

        fun addAbility(ability: Ability) {
            if (ability.id in abilities) return
            val inventory = extendedPlayer?.getPlayer()?.inventory ?: return

            if (abilities[0] == null) abilities[0] = ability.id
            else if (abilities[1] == null) abilities[1] = ability.id
            else {
                abilities[1] = abilities[0]
                abilities[0] = ability.id
            }

            inventory.setItem(3, getAbility1()?.getItem())
            inventory.setItem(4, getAbility2()?.getItem())
        }

        fun getUltimate() = KitPvp.INSTANCE.ultimateHandler.ultimates[ultimate]

        fun setUltimate(ultimate: Ultimate) {
            this.ultimate = ultimate.id
            extendedPlayer?.getPlayer()?.inventory?.setItem(5, ultimate.getItem())
        }

        fun getConsumable1() = KitPvp.INSTANCE.consumableHandler.consumables[consumables[0]]
        fun getConsumable2() = KitPvp.INSTANCE.consumableHandler.consumables[consumables[1]]

        fun addConsumable(consumable: Consumable) {
            if (consumable.id in consumables) return
            val inventory = extendedPlayer?.getPlayer()?.inventory ?: return

            if (consumables[0] == null) consumables[0] = consumable.id
            else if (consumables[1] == null) consumables[1] = consumable.id
            else {
                consumables[1] = consumables[0]
                consumables[0] = consumable.id
            }

            inventory.setItem(7, getConsumable1()?.getStart())
            inventory.setItem(8, getConsumable2()?.getStart())
        }

        fun getPassive() = KitPvp.INSTANCE.passiveHandler.passives[passive]

        fun setPassive(passive: Passive) {
            this.passive = passive.id
            extendedPlayer?.getPlayer()?.inventory?.setItemInOffHand(passive.getItem())
        }

        fun apply() {
            val inventory = extendedPlayer?.getPlayer()?.inventory ?: return

            val blocker = ItemStack.of(Material.STRUCTURE_VOID)
            blocker.editMeta {
                it.isHideTooltip = true
                it.displayName(Component.empty())
            }

            inventory.helmet = getHelmet()?.build()
            inventory.chestplate = getChestplate()?.build()
            inventory.leggings = getLeggings()?.build()
            inventory.boots = getBoots()?.build()

            inventory.setItem(0, getWeapon1()?.build())
            inventory.setItem(1, getWeapon2()?.build())

            inventory.setItem(2, blocker)

            inventory.setItem(3, getAbility1()?.getItem())
            inventory.setItem(4, getAbility2()?.getItem())

            inventory.setItem(5, getUltimate()?.getItem())

            inventory.setItem(6, blocker)

            inventory.setItem(7, getConsumable1()?.getStart())
            inventory.setItem(8, getConsumable2()?.getStart())

            inventory.setItemInOffHand(getPassive()?.getItem())

            inventory.setItem(17, ItemStack.of(Material.ARROW))
        }
    }
}
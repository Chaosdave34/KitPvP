package io.github.chaosdave34.kitpvp.items.impl

import io.github.chaosdave34.kitpvp.items.CustomItem
import org.bukkit.Material

class TurretItem : CustomItem(Material.FIREWORK_ROCKET, "turret", "Turret", preventPlacingAndUsing = true)
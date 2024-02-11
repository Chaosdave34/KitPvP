package net.gamershub.kitpvp.items;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LightingWand extends CustomItem{


  public LightingWand() {
    super(Material.STICK, "lighting_wand", false);
  }

  @Override
  public @NotNull Component getName() {
    return Component.text("Lighting Wand").decoration(TextDecoration.ITALIC, false);
  }

  @Override
  public @NotNull List<Component> getDescription() {
    return new ArrayList<>();
  }

  @Override
  public @NotNull List<Ability> getAbilities() {
    return List.of(AbilityHandler.LIGHTNING);
  }
}


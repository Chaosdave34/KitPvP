package net.gamershub.kitpvp.items;

import net.gamershub.kitpvp.abilities.Ability;
import net.gamershub.kitpvp.abilities.AbilityHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LightningWand extends CustomItem{


  public LightningWand() {
    super(Material.STICK, "lightning_wand", false);
  }

  @Override
  public @NotNull Component getName() {
    return Component.text("Lightning Wand").decoration(TextDecoration.ITALIC, false);
  }

  @Override
  public @NotNull List<Component> getDescription() {
    return List.of(Component.text("Stolen from Zeus.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
  }

  @Override
  public @NotNull List<Ability> getAbilities() {
    return List.of(AbilityHandler.LIGHTNING, AbilityHandler.THUNDERSTORM);
  }
}


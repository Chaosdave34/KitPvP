package net.gamershub.kitpvp.fakeplayer.impl;

import net.gamershub.kitpvp.fakeplayer.FakePlayer;
import net.kyori.adventure.text.Component;
import net.minecraft.world.InteractionHand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class TestFakePlayer extends FakePlayer {
    public TestFakePlayer() {
        super("test", new Location(Bukkit.getWorld("world"), 0, 70, 0), 100, -10, true);

        texture = "eyJ0aW1lc3RhbXAiOjE1ODYyOTYzNTY3NzAsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdmOTMxNTI0OGFlYjZmMGFlOWM1MDI4ZTNiYjdmYzQ3ZGI2NjkyZWRmOWY1OTQ5ZmQ4MmE3MDc2ZjcwMWFmOSIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19";
        textureSignature = "RoP01hUsUJYSlnu7y6auZrAXlMt2PaqFpzeUA4r/SpqenPtBvtgh7UYhTUErx+DfKfZCy6hW3Rzz9WC3ZjShvKqMmGQ/DgZ+lgg6n0s6SbLwhSe2mQcsPPifW+5n+/LA02WB2ZW8jZwGMK8e7ZrOPeesaS/Pjayud51CtwTIAXdT2y6NEAZuJG3Te58Dw3+NecJzBKDhcroykBrEyUXRHXaUbZFVjDnlKHxki6VN7yUvPhUK7iLdJb9RbUxAhbbX/ZPMMV8kew7A+0oVjrEw5+DfymWoTh2VzoFEeIxkvt8oWM/qIy9i9TGdiXSdF7iQz9PvGYOzTI7v/1sW7pKsVQfLPDy6HaPsfjw3rPf6CPk4qkuYqXXC6v8VeH2xov4jL0xiQSk9HznmB4EvSS8FJ7qxfSdRNkjYRvgR5suMF0hUjMKGmhA8EJ5ceFFkJ0KUGemS7qj6bzVYbraDruUIe+vjErACIfPy366h4QOM5xV4v0r/JzfVpoA1DtvcH+v7bK3roHbnb0W/mX3W5PH376Ar2Sk3Ykor/5H/QLmt1/BXrJEpGs+OLFuZ3Jsi2V7Xt+7n9wdfKZrgsmfKrd0qSQefqr1KlwVXIHhgmCWn3QWVdI5P+QLY5o72varcu2adfCCJywD375dRDl/hWhkvVhiYhaoI41PXphbjo1tYhHk=";

        equipment.put(EquipmentSlot.HAND, new ItemStack(Material.DIAMOND_SWORD));
        equipment.put(EquipmentSlot.OFF_HAND, new ItemStack(Material.DIAMOND_SWORD));
        equipment.put(EquipmentSlot.HEAD, new ItemStack(Material.RED_STAINED_GLASS));
    }

    @Override
    public void onAttack(Player p) {
        p.sendMessage(Component.text("Attack"));
        move(new Location(position.getWorld(), 3, 70, 3));
    }

    @Override
    public void onInteract(Player p, InteractionHand hand) {
        p.sendMessage(Component.text("Interact " + hand.toString()));
    }
}

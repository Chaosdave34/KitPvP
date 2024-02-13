package net.gamershub.kitpvp.fakeplayer.impl;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.fakeplayer.FakePlayer;
import net.gamershub.kitpvp.kits.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class KitSelectorFakePlayer extends FakePlayer {
    private final Kit kit;

    public KitSelectorFakePlayer(Kit kit, Location location) {
        super(kit.getName(), location, location.getYaw(), location.getPitch(), true);
        this.kit = kit;

        equipment.put(EquipmentSlot.FEET, kit.getArmorContents()[0]);
        equipment.put(EquipmentSlot.LEGS, kit.getArmorContents()[1]);
        equipment.put(EquipmentSlot.CHEST, kit.getArmorContents()[2]);
        equipment.put(EquipmentSlot.HEAD, kit.getArmorContents()[3]);
    }

    @Override
    public void onAttack(Player p) {
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getSelectedKit() == kit) {
            p.sendMessage(Component.text("You have already selected this kit!"));
        } else {

            kit.apply(p);

            p.sendMessage(Component.text("You have selected ", NamedTextColor.GRAY)
                    .append(Component.text(kit.getName(), NamedTextColor.GREEN))
                    .append(Component.text(".", NamedTextColor.GRAY)));
        }
    }
}

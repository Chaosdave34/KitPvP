package net.gamershub.kitpvp.fakeplayer.impl;

import net.gamershub.kitpvp.ExtendedPlayer;
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

        equipment.put(EquipmentSlot.HEAD, kit.getHeadContent());
        equipment.put(EquipmentSlot.CHEST, kit.getChestContent());
        equipment.put(EquipmentSlot.LEGS, kit.getLegsContent());
        equipment.put(EquipmentSlot.FEET, kit.getFeetContent());
        equipment.put(EquipmentSlot.HAND, kit.getInventoryContent()[0]);
        equipment.put(EquipmentSlot.OFF_HAND, kit.getOffhandContent());

    }

    @Override
    public void onAttack(Player p) {
        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getGameState() == ExtendedPlayer.GameState.IN_GAME) return;

        if (KitPvpPlugin.INSTANCE.getExtendedPlayer(p).getSelectedKit() == kit) {
            p.sendMessage(Component.text("You have already selected this kit!", NamedTextColor.GRAY));
        } else {

            kit.apply(p);

            p.sendMessage(Component.text("You have selected the Kit ", NamedTextColor.GRAY)
                    .append(Component.text(kit.getName(), NamedTextColor.GREEN))
                    .append(Component.text(".", NamedTextColor.GRAY)));
        }
    }
}

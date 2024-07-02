package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghutils.fakeplayer.FakePlayer;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.Utils;
import io.github.chaosdave34.kitpvp.kits.Kit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.entity.Mob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Obsolete
public class KitSelectorFakePlayer extends FakePlayer {
    private final Kit kit;

    public KitSelectorFakePlayer(Kit kit, String worldName, Location position) {
        super(kit.getName(), worldName, position, position.getYaw(), position.getPitch(), true);
        this.kit = kit;

        equipment.put(EquipmentSlot.HEAD, kit.getHeadContent());
        equipment.put(EquipmentSlot.CHEST, kit.getChestContent());
        equipment.put(EquipmentSlot.LEGS, kit.getLegsContent());
        equipment.put(EquipmentSlot.FEET, kit.getFeetContent());
        equipment.put(EquipmentSlot.HAND, kit.getInventoryContent()[0]);
        equipment.put(EquipmentSlot.OFF_HAND, kit.getOffhandContent());
    }

    @Override
    public void spawn(Player p) {
        super.spawn(p);

        Location dummyCompanionLocation = getPosition().clone().add(CraftVector.toBukkit(serverPlayer.getLookAngle()).rotateAroundY(Math.toRadians(45)));
        dummyCompanionLocation.setWorld(Bukkit.getWorld(worldName));

        if (kit.getCompanion() != null) {
            Mob mob = kit.getCompanion().createDummyCompanion(dummyCompanionLocation);
            Utils.spawnNmsEntity(p, mob);
        }
    }

    @Override
    public void onAttack(@NotNull Player p) {
        selectKit(p);
    }

    @Override
    public void onActualInteract(@NotNull Player p) {
        selectKit(p);
    }

    private void selectKit(Player p) {
        if (ExtendedPlayer.from(p).inGame()) return;

        if (ExtendedPlayer.from(p).getSelectedKitsKit() == kit) {
            p.sendMessage(Component.text("You have already selected this kit!", NamedTextColor.GRAY));
        } else {
            kit.apply(p);

            p.sendMessage(Component.text("You have selected the Kit ", NamedTextColor.GRAY)
                    .append(Component.text(kit.getName(), NamedTextColor.GREEN))
                    .append(Component.text(".", NamedTextColor.GRAY)));
        }
    }
}

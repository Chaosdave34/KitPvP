package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghutils.fakeplayer.FakePlayer;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ElytraFakePlayer extends FakePlayer {
    public ElytraFakePlayer() {
        super("ElytraPvP", "world", new Location(null, -1.5, 120, 6.5), -45, 0, true);

        equipment.put(EquipmentSlot.CHEST, new ItemStack(Material.ELYTRA));
    }

    @Override
    public void onAttack(Player p) {
        ExtendedPlayer.from(p).spawn(ExtendedPlayer.GameType.ELYTRA);
    }

    @Override
    public void onInteract(Player p, EquipmentSlot hand) {
        ExtendedPlayer.from(p).spawn(ExtendedPlayer.GameType.ELYTRA);
    }
}

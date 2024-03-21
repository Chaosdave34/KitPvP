package net.gamershub.kitpvp.textdisplays;

import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.textdisplays.impl.*;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.ArrayList;
import java.util.List;

public class TextDisplayHandler implements Listener {
    List<TextDisplay> textDisplays = new ArrayList<>();

    public static TextDisplay JUMP;
    public static TextDisplay PERSONAL_STATISTICS;
    public static TextDisplay INFO;
    public static TextDisplay HIGHEST_KILLSTREAKS;
    public static TextDisplay HIGHEST_LEVELS;
    public static TextDisplay AMUSEMENT_PARK_TEST_DISPLAY;

    public TextDisplayHandler() {
        JUMP = registerTextDisplay(new JumpTextDisplay());
        PERSONAL_STATISTICS = registerTextDisplay(new PersonalStatisticsDisplay());
        INFO = registerTextDisplay(new InfoTextDisplay());
        HIGHEST_KILLSTREAKS = registerTextDisplay(new HighestKillstreaksTextDisplay());
        HIGHEST_LEVELS = registerTextDisplay(new HighestLevelsTextDisplay());
        AMUSEMENT_PARK_TEST_DISPLAY = registerTextDisplay(new AmusementParkTextDisplay());
    }

    private TextDisplay registerTextDisplay(TextDisplay textDisplay) {
        textDisplays.add(textDisplay);

        return textDisplay;
    }

    private void createTextDisplay(TextDisplay textDisplay) {
        Location position = textDisplay.getPosition();
        World world = Bukkit.getWorld(textDisplay.getWorldName());
        if (world == null) return;

        ServerLevel level = ((CraftWorld) world).getHandle();

        for (int i = 0; i < textDisplay.getLineCount(); i++) {
            ArmorStand armorStand = new ArmorStand(level, position.x(), position.y() + i * -0.3, position.z());
            armorStand.setSmall(true);
            armorStand.setInvisible(true);
            armorStand.setInvulnerable(true);

            armorStand.setCustomNameVisible(true);

            textDisplay.getArmorStands().add(armorStand);
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        World world = e.getWorld();

        for (TextDisplay textDisplay : textDisplays) {
            if (world.getName().equals(textDisplay.getWorldName())) {
                createTextDisplay(textDisplay);
            }
        }
    }

    public void spawnTextDisplays(Player p) {
        for (TextDisplay textDisplay : textDisplays) {
            for (int i = 0; i < textDisplay.getLineCount(); i++) {
                ArmorStand armorStand = textDisplay.getArmorStands().get(i);
                armorStand.setCustomName(textDisplay.getLines(p).get(i));

                Utils.spawnNmsEntity(p, armorStand);
            }
        }
    }

    public void updateTextDisplayForAll(TextDisplay textDisplay) {
        Bukkit.getOnlinePlayers().forEach((player) -> updateTextDisplay(player, textDisplay));
    }

    public void updateTextDisplay(Player p, TextDisplay textDisplay) {
        CraftPlayer cp = (CraftPlayer) p;
        ServerPlayer sp = cp.getHandle();
        ServerGamePacketListenerImpl connection = sp.connection;

        for (int i = 0; i < textDisplay.getLineCount(); i++) {
            ArmorStand armorStand = textDisplay.getArmorStands().get(i);
            armorStand.setCustomName(textDisplay.getLines(p).get(i));

            List<SynchedEntityData.DataValue<?>> nonDefaultValues = armorStand.getEntityData().getNonDefaultValues();
            if (nonDefaultValues != null) {
                ClientboundSetEntityDataPacket setEntityDataPacket = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData().getNonDefaultValues());
                connection.send(setEntityDataPacket);
            }
        }
    }
}

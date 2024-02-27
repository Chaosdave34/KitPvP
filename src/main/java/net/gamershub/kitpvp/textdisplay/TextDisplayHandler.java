package net.gamershub.kitpvp.textdisplay;

import net.gamershub.kitpvp.textdisplay.impl.HighestKillstreaksTextDisplay;
import net.gamershub.kitpvp.textdisplay.impl.JumpTextDisplay;
import net.gamershub.kitpvp.textdisplay.impl.PersonalStatisticsDisplay;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TextDisplayHandler {
    List<TextDisplay> textDisplays = new ArrayList<>();

    public static TextDisplay JUMP;
    public static TextDisplay PERSONAL_STATISTICS;
    public static TextDisplay HIGHEST_KILLSTREAKS;

    public TextDisplayHandler() {
        JUMP = createTextDisplay(new JumpTextDisplay());
        PERSONAL_STATISTICS = createTextDisplay(new PersonalStatisticsDisplay());
        HIGHEST_KILLSTREAKS = createTextDisplay(new HighestKillstreaksTextDisplay());
    }

    private TextDisplay createTextDisplay(TextDisplay textDisplay) {
        Location position = textDisplay.getPosition();
        ServerLevel level = ((CraftWorld) position.getWorld()).getHandle();

        for (int i = 0; i< textDisplay.getLineCount(); i++) {
            ArmorStand armorStand = new ArmorStand(level, position.x(), position.y() + i * -0.3, position.z());
            armorStand.setSmall(true);
            armorStand.setInvisible(true);
            armorStand.setInvulnerable(true);

            armorStand.setCustomNameVisible(true);

            textDisplay.getArmorStands().add(armorStand);
        }

        textDisplays.add(textDisplay);

        return textDisplay;
    }

    public void spawnTextDisplays(Player p) {
        for (TextDisplay textDisplay : textDisplays) {
            CraftPlayer cp = (CraftPlayer) p;
            ServerPlayer sp = cp.getHandle();
            ServerGamePacketListenerImpl connection = sp.connection;

            for (int i = 0; i < textDisplay.getLineCount(); i++) {
                ArmorStand armorStand = textDisplay.getArmorStands().get(i);
                armorStand.setCustomName(textDisplay.getLines(p).get(i));

                ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(armorStand);
                connection.send(addEntityPacket);

                List<SynchedEntityData.DataValue<?>> nonDefaultValues = armorStand.getEntityData().getNonDefaultValues();
                if (nonDefaultValues != null) {
                    ClientboundSetEntityDataPacket setEntityDataPacket = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData().getNonDefaultValues());
                    connection.send(setEntityDataPacket);
                }
            }
        }
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

package net.gamershub.kitpvp.fakeplayer;

import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import lombok.Setter;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Getter
public abstract class FakePlayer {
    @Setter
    protected ServerPlayer serverPlayer;

    protected String name;
    protected String worldName;
    protected Location position;
    protected float yaw;
    protected float pitch;
    protected float yawHeadRotation;
    protected boolean showOnPlayerSpawn;

    protected String texture;
    protected String textureSignature;
    protected Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();
    protected Pose pose;

    public FakePlayer(String name, String worldName, Location position, float yaw, float pitch, boolean showOnPlayerSpawn) {
        this.name = name;
        this.worldName = worldName;
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.yawHeadRotation = yaw;
        this.showOnPlayerSpawn = showOnPlayerSpawn;
    }

    public void onAttack(Player p) {
    }

    public void onInteract(Player p, EquipmentSlot hand) {
    }

    protected void updatePose(Pose pose) {
        if (this.pose != pose) {
            this.pose = pose;
            KitPvpPlugin.INSTANCE.getFakePlayerHandler().updatePose(this);
        }
    }

    protected void teleport(Location position) {
        if (!this.position.equals(position)) {
            this.position = position;
            KitPvpPlugin.INSTANCE.getFakePlayerHandler().teleport(this);
        }
    }

    protected void move(Location position) {
        if (!this.position.equals(position)) {
            short deltaX = (short) (position.x() - this.position.x());
            short deltaY = (short) (position.y() - this.position.y());
            short deltaZ = (short) (position.z() - this.position.z());

            if (Math.abs(deltaX) > 8 || Math.abs(deltaY) > 8 || Math.abs(deltaZ) > 8) return;

            deltaX = (short) ((position.x() * 32 - this.position.x() * 32) * 128);
            deltaY = (short) ((position.y() * 32 - this.position.y() * 32) * 128);
            deltaZ = (short) ((position.z() * 32 - this.position.z() * 32) * 128);

            this.position = position;

            KitPvpPlugin.INSTANCE.getFakePlayerHandler().move(this, deltaX, deltaY, deltaZ);
        }
    }

    protected void updateEquipment(EquipmentSlot slot, ItemStack itemStack) {
        if (!itemStack.equals(equipment.get(slot))) {
            equipment.put(slot, itemStack);
            KitPvpPlugin.INSTANCE.getFakePlayerHandler().updateEquipment(this);
        }
    }

    protected void animate(Animation animation) {
        KitPvpPlugin.INSTANCE.getFakePlayerHandler().animate(this, animation.getId());
    }

    protected void playHurtAnimation() {
        KitPvpPlugin.INSTANCE.getFakePlayerHandler().playHurtAnimation(this);
    }

    protected void updateHandState(boolean active, InteractionHand hand) {
        KitPvpPlugin.INSTANCE.getFakePlayerHandler().updateHandState(this, active, hand);
    }

    public void spawn(Player p) {
        CraftPlayer cp = (CraftPlayer) p;
        ServerPlayer sp = cp.getHandle();
        ServerGamePacketListenerImpl connection = sp.connection;

        ServerPlayer npc = serverPlayer;
        ClientboundPlayerInfoUpdatePacket infoPacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc);
        connection.send(infoPacket);

        Utils.spawnNmsEntity(p, npc);

        List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipment = new ArrayList<>();
        for (Map.Entry<EquipmentSlot, ItemStack> entry : getEquipment().entrySet()) {
            equipment.add(new Pair<>(CraftEquipmentSlot.getNMS(entry.getKey()), CraftItemStack.asNMSCopy(entry.getValue())));
        }
        if (!equipment.isEmpty()) {
            ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(npc.getId(), equipment);
            connection.send(equipmentPacket);
        }
    }

    protected void despawn() {
        ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(serverPlayer.getId());
        Utils.sendPacketToOnlinePlayers(removeEntitiesPacket);
    }

    @Getter
    protected enum Animation {
        SWING_MAIN_HAND(0),
        WAKE_UP(2),
        SWING_OFF_HAND(3),
        CRITICAL_HIT(4),
        MAGIC_CRITICAL_HIT(5);

        private final int id;

        Animation(int id) {
            this.id = id;
        }
    }
}

package net.gamershub.kitpvp.fakeplayer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import io.netty.channel.embedded.EmbeddedChannel;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.fakeplayer.impl.TestFakePlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.ChatVisiblity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FakePlayerHandler {
    List<FakePlayer> fakePlayers = new ArrayList<>();

    public static FakePlayer TEST;

    public FakePlayerHandler() {
        TEST = createFakePlayer(new TestFakePlayer());
    }

    public FakePlayer createFakePlayer(FakePlayer fakePlayer) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel level = ((CraftWorld) fakePlayer.getPosition().getWorld()).getHandle();

        GameProfile profile = new GameProfile(UUID.randomUUID(), fakePlayer.getName());
        ClientInformation info = new ClientInformation("de_de", 2, ChatVisiblity.FULL, true, 127, net.minecraft.world.entity.player.Player.DEFAULT_MAIN_HAND, false, false);

        ServerPlayer npc = new ServerPlayer(server, level, profile, info);
        npc.setPos(fakePlayer.getPosition().x(), fakePlayer.getPosition().y(), fakePlayer.getPosition().z());
        npc.setRot(fakePlayer.getYaw(), fakePlayer.getPitch());
        npc.setYHeadRot(fakePlayer.getYawHeadRotation());

        if (fakePlayer.getTexture() != null)
            profile.getProperties().put("textures", new Property("textures", fakePlayer.getTexture(), fakePlayer.textureSignature));


        if (fakePlayer.getPose() != null)
            npc.setPose(fakePlayer.getPose());


        Connection c = new Connection(PacketFlow.SERVERBOUND);
        c.channel = new EmbeddedChannel();
        c.channel.attr(Connection.ATTRIBUTE_CLIENTBOUND_PROTOCOL).set(ConnectionProtocol.PLAY.codec(PacketFlow.CLIENTBOUND));
        c.channel.attr(Connection.ATTRIBUTE_SERVERBOUND_PROTOCOL).set(ConnectionProtocol.PLAY.codec(PacketFlow.SERVERBOUND));

        c.address = new InetSocketAddress("localhost", 0);
        new ServerGamePacketListenerImpl(server, c, npc, new CommonListenerCookie(profile, 0, info));

        fakePlayer.setServerPlayer(npc);
        fakePlayers.add(fakePlayer);

        return fakePlayer;
    }

    public void spawnFakePlayers(Player p) {
        for (FakePlayer fakePlayer : fakePlayers) {
            if (fakePlayer.isShowOnPlayerSpawn())
                spawnFakePlayer(p, fakePlayer);
        }
    }

    public void spawnFakePlayer(Player p, FakePlayer fakePlayer) {
        CraftPlayer cp = (CraftPlayer) p;
        ServerPlayer sp = cp.getHandle();
        ServerGamePacketListenerImpl connection = sp.connection;

        ServerPlayer npc = fakePlayer.serverPlayer;
        ClientboundPlayerInfoUpdatePacket infoPacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc);
        connection.send(infoPacket);

        ClientboundAddEntityPacket addNpcPacket = new ClientboundAddEntityPacket(npc);
        connection.send(addNpcPacket);

        List<SynchedEntityData.DataValue<?>> nonDefaultValues = npc.getEntityData().getNonDefaultValues();
        if (nonDefaultValues != null) {
            ClientboundSetEntityDataPacket entityDataPacket = new ClientboundSetEntityDataPacket(npc.getId(), nonDefaultValues);
            connection.send(entityDataPacket);
        }

        List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipment = new ArrayList<>();
        for (Map.Entry<EquipmentSlot, ItemStack> entry : fakePlayer.getEquipment().entrySet()) {
            equipment.add(new Pair<>(CraftEquipmentSlot.getNMS(entry.getKey()), CraftItemStack.asNMSCopy(entry.getValue())));
        }
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(npc.getId(), equipment);
        connection.send(equipmentPacket);
    }

    private FakePlayer getFakePlayerByID(int id) {
        for (FakePlayer fakePlayer : fakePlayers) {
            if (fakePlayer.serverPlayer.getId() == id) return fakePlayer;
        }
        return null;
    }

    public void onPlayerInteractPacket(Player p, ServerboundInteractPacket packet) {
        FakePlayer fakePlayer = getFakePlayerByID(packet.getEntityId());
        if (fakePlayer != null) {

            if (packet.isAttack()) {
                fakePlayer.onAttack(p);
            } else {
                Object action = Utils.getPrivateField(packet, "action");
                try {
                    Method getType = action.getClass().getDeclaredMethod("getType");
                    getType.setAccessible(true);
                    Object response = getType.invoke(action);
                    String actionType = response.toString();
                    getType.setAccessible(false);

                    InteractionHand hand = (InteractionHand) Utils.getPrivateField(action, "hand");

                    if (actionType.equals("INTERACT")) {
                        fakePlayer.onInteract(p, hand);
                    }

                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    KitPvpPlugin.INSTANCE.getLogger().warning("Error while trying to get type of interaction in ServerboundInteractionPacket");
                }
            }

        }
    }

    private void updateEntityMetadata(FakePlayer fakePlayer, SynchedEntityData.DataValue<?> dataValue) {
        ClientboundSetEntityDataPacket entityDataPacket = new ClientboundSetEntityDataPacket(fakePlayer.serverPlayer.getId(), List.of(dataValue));
        Utils.sendPacketToOnlinePlayers(entityDataPacket);
    }

    public void updatePose(FakePlayer fakePlayer) {
        Bukkit.getScheduler().runTask(KitPvpPlugin.INSTANCE, () -> fakePlayer.serverPlayer.setPose(fakePlayer.pose));

        SynchedEntityData.DataItem<Pose> dataItem = new SynchedEntityData.DataItem<>(new EntityDataAccessor<>(6, EntityDataSerializers.POSE), fakePlayer.pose);
        updateEntityMetadata(fakePlayer, dataItem.value());
    }

    public void teleport(FakePlayer fakePlayer) {
        fakePlayer.serverPlayer.setPos(fakePlayer.getPosition().x(), fakePlayer.getPosition().y(), fakePlayer.getPosition().z());

        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(fakePlayer.serverPlayer);
        Utils.sendPacketToOnlinePlayers(teleportEntityPacket);
    }

    public void move(FakePlayer fakePlayer, short deltax, short deltay, short deltaZ) {
        fakePlayer.serverPlayer.setPos(fakePlayer.getPosition().x(), fakePlayer.getPosition().y(), fakePlayer.getPosition().z());

        ClientboundMoveEntityPacket moveEntityPacket = new ClientboundMoveEntityPacket.Pos(fakePlayer.serverPlayer.getId(), deltax, deltay, deltaZ, false);
        Utils.sendPacketToOnlinePlayers(moveEntityPacket);
    }

    public void updateEquipment(FakePlayer fakePlayer) {
        List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipment = new ArrayList<>();
        for (Map.Entry<EquipmentSlot, ItemStack> entry : fakePlayer.getEquipment().entrySet()) {
            equipment.add(new Pair<>(CraftEquipmentSlot.getNMS(entry.getKey()), CraftItemStack.asNMSCopy(entry.getValue())));
        }

        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(fakePlayer.serverPlayer.getId(), equipment);
        Utils.sendPacketToOnlinePlayers(equipmentPacket);
    }

    public void animate(FakePlayer fakePlayer, int animation) {
        ClientboundAnimatePacket animatePacket = new ClientboundAnimatePacket(fakePlayer.serverPlayer, animation);
        Utils.sendPacketToOnlinePlayers(animatePacket);
    }

    public void updateHandState(FakePlayer fakePlayer, boolean active, InteractionHand hand) {
        int value = 0;
        if (active) value += 1;
        if (hand == InteractionHand.OFF_HAND) value += 2;

        SynchedEntityData.DataItem<Byte> dataItem = new SynchedEntityData.DataItem<>(new EntityDataAccessor<>(8, EntityDataSerializers.BYTE), (byte) value);
        updateEntityMetadata(fakePlayer, dataItem.value());
    }

    public void playHurtAnimation(FakePlayer fakePlayer) {
        ClientboundHurtAnimationPacket hurtAnimationPacket = new ClientboundHurtAnimationPacket(fakePlayer.serverPlayer);
        Utils.sendPacketToOnlinePlayers(hurtAnimationPacket);
    }

    public void despawn(FakePlayer fakePlayer) {
        ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(fakePlayer.serverPlayer.getId());
        Utils.sendPacketToOnlinePlayers(removeEntitiesPacket);
    }
}

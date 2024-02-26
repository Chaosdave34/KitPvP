package net.gamershub.kitpvp.fakeplayer;

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import io.netty.channel.embedded.EmbeddedChannel;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.fakeplayer.impl.KitSelectorFakePlayer;
import net.gamershub.kitpvp.kits.KitHandler;
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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FakePlayerHandler implements Listener {
    List<FakePlayer> fakePlayers = new ArrayList<>();

    public static FakePlayer CLASSIC_KIT;
    public static FakePlayer ZEUS_KIT;
    public static FakePlayer TANK_KIT;
    public static FakePlayer PROVOKER_KIT;
    public static FakePlayer ARCHER_KIT;
    public static FakePlayer CROSSBOW_KIT;
    public static FakePlayer RUNNER_KIT;
    public static FakePlayer TRAPPER_KIT;
    public static FakePlayer MAGICIAN_KIT;
    public static FakePlayer VAMPIRE_KIT;

    public FakePlayerHandler() {
        World world = Bukkit.getWorld("world");
        CLASSIC_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.CLASSIC, new Location(world, 10.5, 100, 0.5, 90, 0)));
        ZEUS_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.ZEUS, new Location(world, 10.5, 100, 2.5, 90, 0)));
        TANK_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.TANK, new Location(world, 10.5, 100, 4.5, 90, 0)));
        PROVOKER_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.PROVOKER, new Location(world, 10.5, 100, 6.5, 90, 0)));
        ARCHER_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.ARCHER, new Location(world, 10.5, 100, -1.5, 90, 0)));
        CROSSBOW_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.CROSSBOW, new Location(world, 10.5, 100, -3.5, 90, 0)));
        RUNNER_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.RUNNER, new Location(world, 10.5, 100, -5.5, 90, 0)));


        TRAPPER_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.TRAPPER, new Location(world, 9.5, 100, -4.5, 90, 0)));
        MAGICIAN_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.MAGICIAN, new Location(world, 9.5, 100, -2.5, 90, 0)));
        VAMPIRE_KIT = createFakePlayer(new KitSelectorFakePlayer(KitHandler.VAMPIRE, new Location(world, 9.5, 100, -0.5, 90, 0)));
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

    @EventHandler
    public void onPlayerInteract(PlayerUseUnknownEntityEvent e) {
        Player p = e.getPlayer();
        FakePlayer fakePlayer = getFakePlayerByID(e.getEntityId());
        if (fakePlayer != null) {

            if (e.isAttack()) {
                fakePlayer.onAttack(p);
            } else {
                fakePlayer.onInteract(p, e.getHand());
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

    public void move(FakePlayer fakePlayer, short deltaX, short deltaY, short deltaZ) {
        fakePlayer.serverPlayer.setPos(fakePlayer.getPosition().x(), fakePlayer.getPosition().y(), fakePlayer.getPosition().z());

        ClientboundMoveEntityPacket moveEntityPacket = new ClientboundMoveEntityPacket.Pos(fakePlayer.serverPlayer.getId(), deltaX, deltaY, deltaZ, false);
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

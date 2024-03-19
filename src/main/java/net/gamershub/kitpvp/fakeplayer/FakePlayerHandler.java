package net.gamershub.kitpvp.fakeplayer;

import com.destroystokyo.paper.event.player.PlayerUseUnknownEntityEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import io.netty.channel.embedded.EmbeddedChannel;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.Utils;
import net.gamershub.kitpvp.fakeplayer.impl.CosmeticsFakePlayer;
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
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
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
    public static FakePlayer ENGINEER_KIT;
    public static FakePlayer ARCHER_KIT;
    public static FakePlayer ARTILLERYMAN_KIT;
    public static FakePlayer RUNNER_KIT;
    public static FakePlayer TRAPPER_KIT;
    public static FakePlayer MAGICIAN_KIT;
    public static FakePlayer VAMPIRE_KIT;
    public static FakePlayer CREEPER_KIT;
    public static FakePlayer ENDERMAN_KIT;
    public static FakePlayer POSEIDON_KIT;
    public static FakePlayer DEVIL_KIT;

    public static FakePlayer COSMETIC;

    public FakePlayerHandler() {
        CLASSIC_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.CLASSIC, "world", new Location(null, -6.5, 120.0, 9.5, -90, 0)));
        ZEUS_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.ZEUS, "world", new Location(null, -7.5, 120.0, 6.5, -90, 0)));
        TANK_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.TANK, "world", new Location(null, -8.5, 120.0, 3.5, -90, 0)));
        ENGINEER_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.PROVOKER, "world", new Location(null, -8.5, 120.0, 0.5, -90, 0)));
        ARCHER_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.ARCHER, "world", new Location(null, -7.5, 120.0, -2.5, -90, 0)));
        ARTILLERYMAN_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.ARTILLERYMAN, "world", new Location(null, -6.5, 120.0, -5.5, -65, 0)));
        RUNNER_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.ASSASSIN, "world", new Location(null, -4.5, 120.0, -7.5, -35, 0)));

        TRAPPER_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.ENGINEER, "world", new Location(null, 10.5, 120.0, 9.5, 90, 0)));
        MAGICIAN_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.MAGICIAN, "world", new Location(null, 11.5, 120.0, 6.5, 90, 0)));
        VAMPIRE_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.VAMPIRE, "world", new Location(null, 12.5, 120.0, 3.5, 90, 0)));
        CREEPER_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.CREEPER, "world", new Location(null, 12.5, 120.0, 0.5, 90, 0)));
        ENDERMAN_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.ENDERMAN, "world", new Location(null, 11.5, 120.0, -2.5, 90, 0)));
        POSEIDON_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.POSEIDON, "world", new Location(null, 10.5, 120.0, -5.5, 65, 0)));
        DEVIL_KIT = registerFakePlayer(new KitSelectorFakePlayer(KitHandler.DEVIL, "world", new Location(null, 8.5, 120.0, -7.5, 45, 0)));

        COSMETIC = registerFakePlayer(new CosmeticsFakePlayer());
    }

    public FakePlayer registerFakePlayer(FakePlayer fakePlayer) {
        fakePlayers.add(fakePlayer);
        return fakePlayer;
    }

    private void createFakePlayer(FakePlayer fakePlayer) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        World world = Bukkit.getWorld(fakePlayer.getWorldName());
        if (world == null) return;

        ServerLevel level = ((CraftWorld) world).getHandle();

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
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        World world = e.getWorld();

        for (FakePlayer fakePlayer : fakePlayers) {
            if (world.getName().equals(fakePlayer.getWorldName())) {
                createFakePlayer(fakePlayer);
            }
        }
    }

    public void spawnFakePlayers(Player p) {
        for (FakePlayer fakePlayer : fakePlayers) {
            if (fakePlayer.isShowOnPlayerSpawn())
                fakePlayer.spawn(p);
        }
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
}

package net.gamershub.kitpvp;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class Utils {
    public static Object getPrivateField(Object instance, String name) {
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);

            field.setAccessible(true);

            result = field.get(instance);

            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            KitPvpPlugin.INSTANCE.getLogger().warning("Error while getting private field.");
        }
        return result;
    }

    public static void sendPacketToOnlinePlayers(Packet<?> packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            CraftPlayer cp = (CraftPlayer) player;
            ServerPlayer sp = cp.getHandle();
            ServerGamePacketListenerImpl connection = sp.connection;

            connection.send(packet);
        }
    }

    public static void registerEvents(@NotNull Listener listener) {
        KitPvpPlugin.INSTANCE.getServer().getPluginManager().registerEvents(listener, KitPvpPlugin.INSTANCE);
    }
}
